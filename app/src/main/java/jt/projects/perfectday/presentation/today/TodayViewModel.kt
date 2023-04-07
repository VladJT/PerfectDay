package jt.projects.perfectday.presentation.today

import android.util.Log
import androidx.lifecycle.viewModelScope
import jt.projects.model.AppState
import jt.projects.model.DataModel
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.utils.FACTS_COUNT
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.VK_AUTH_TOKEN
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.time.LocalDate

class TodayViewModel(
    private val settingsPreferences: SimpleSettingsPreferences,
    private val birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    private val simpleNoticeInteractorImpl: SimpleNoticeInteractorImpl,
    private val getFriendsFromVkUseCase: GetFriendsFromVkUseCase
) : BaseViewModel<AppState>() {
    private val currentDate = LocalDate.now()
    private val vkToken: String? by lazy { settingsPreferences.getSettings(VK_AUTH_TOKEN) }

    /* В дальнейшем хорошо бы делать логику асинхронно, сейчас у нас в коррутине запрос
    * дергается один за другим, тем самым данные могут подгружаться дольше.
    * или если один из запросов упадет, то остальные не выполнятся*/
    fun loadData() {
        liveData.value = AppState.Loading(0)

        viewModelScope.launch {
            val data = mutableListOf<DataModel>()

            try {
                //Пока не добавил в адаптер
                val friendsFromVk = loadFriendsFromVk()
                Log.d("TAG", "friendsVk= $friendsFromVk")

                val dataByDate = birthdayFromPhoneInteractor.getDataByDate(currentDate)
                liveData.value = AppState.Loading(50)
                val factsByDate =
                    simpleNoticeInteractorImpl.getFactsByDate(currentDate, FACTS_COUNT)

                data.addAll(dataByDate)
                data.addAll(factsByDate)

                liveData.value = AppState.Loading(100)
                liveData.postValue(AppState.Success(data))

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                liveData.postValue(AppState.Error(e))
            }
        }

    }

    private suspend fun loadFriendsFromVk(): List<DataModel> {
        if (vkToken == null || vkToken!!.isEmpty()) return emptyList()
        return getFriendsFromVkUseCase.getFriends(vkToken!!)
    }

    // Список данных получен
    override fun handleResponse(response: AppState) {
        liveData.postValue(response)
    }

    // Обрабатываем ошибки
    override fun handleError(error: Throwable) {
        liveData.postValue(AppState.Error(error))
        cancelJob()
    }

    override fun onCleared() {
        liveData.value = AppState.Success(null)
        super.onCleared()
    }
}
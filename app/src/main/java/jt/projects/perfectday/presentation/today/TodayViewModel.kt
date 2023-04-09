package jt.projects.perfectday.presentation.today

import android.util.Log
import androidx.lifecycle.viewModelScope
import jt.projects.model.AppState
import jt.projects.model.DataModel
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.utils.FACTS_COUNT
import jt.projects.utils.LOG_TAG
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.VK_AUTH_TOKEN
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.time.LocalDate

class TodayViewModel(
    private val settingsPreferences: SimpleSettingsPreferences,
    private val birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    private val simpleNoticeInteractorImpl: SimpleNoticeInteractorImpl,
    private val getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    private val scheduledEventInteractorImpl: ScheduledEventInteractorImpl
) : BaseViewModel<AppState>() {
    private val currentDate = LocalDate.now()
    private val vkToken: String? by lazy { settingsPreferences.getSettings(VK_AUTH_TOKEN) }

    private val data = mutableListOf<DataModel>()

    /* В дальнейшем хорошо бы делать логику асинхронно, сейчас у нас в коррутине запрос
    * дергается один за другим, тем самым данные могут подгружаться дольше.
    * или если один из запросов упадет, то остальные не выполнятся*/
    fun loadData() {
        liveData.value = AppState.Loading(0)
        data.clear()

        viewModelScope.launch {
            try {
                loadBirthdaysFromVk()
                liveData.value = AppState.Loading(20)

                loadBirthdaysFromPhone()
                liveData.value = AppState.Loading(40)

                loadInterestingFacts()
                liveData.value = AppState.Loading(60)

                loadHolidays()
                liveData.value = AppState.Loading(80)

                loadScheduledEvents()
                liveData.value = AppState.Loading(100)

                liveData.postValue(AppState.Success(data))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                liveData.postValue(AppState.Error(e))
            }
        }

    }

    private fun loadBirthdaysFromPhone() {
        try {
            val dataByDate = birthdayFromPhoneInteractor.getDataByDate(currentDate)
            data.addAll(dataByDate)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    private suspend fun loadBirthdaysFromVk() {
        try {
            //Пока не добавил в адаптер
            val friendsFromVk = loadFriendsFromVk()
            Log.d("TAG", "friendsVk= $friendsFromVk")
        } catch (e: Exception) {
            handleError(e)
        }
    }

    private suspend fun loadInterestingFacts() {
        try {
            val factsByDate =
                simpleNoticeInteractorImpl.getFactsByDate(currentDate, FACTS_COUNT)
            data.addAll(factsByDate)
        } catch (e: Exception) {
            handleError(e)
        }

    }

    private suspend fun loadHolidays() {
        try {
            throw Exception("some error")
        } catch (e: Exception) {
            handleError(e)
        }
    }

    private suspend fun loadScheduledEvents() {
        try {
            val scheduledEvents = scheduledEventInteractorImpl.getScheduledEventsByDate(currentDate)
            data.addAll(scheduledEvents)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    private suspend fun reloadScheduledEvents() {
        data.removeAll { it is DataModel.ScheduledEvent }
        loadScheduledEvents()
    }


    fun deleteScheduledEvent(id: Int) {
        viewModelScope.launch {
            scheduledEventInteractorImpl.deleteScheduledEventById(id)
            reloadScheduledEvents()
        }
    }

    private suspend fun loadFriendsFromVk(): List<DataModel> {
        if (vkToken == null || vkToken!!.isEmpty()) return emptyList()
        return getFriendsFromVkUseCase.getFriends(vkToken!!)
    }

    override fun onCleared() {
        liveData.value = AppState.Success(null)
        super.onCleared()
    }
}
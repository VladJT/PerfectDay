package jt.projects.perfectday.presentation.today

import androidx.lifecycle.viewModelScope
import jt.projects.model.AppState
import jt.projects.model.DataModel
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.utils.FACTS_COUNT
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.time.LocalDate

class TodayViewModel(
    private val birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    private val simpleNoticeInteractorImpl: SimpleNoticeInteractorImpl

) : BaseViewModel<AppState>() {

    private val currentDate = LocalDate.now()

    /* В дальнейшем хорошо бы делать логику асинхронно, сейчас у нас в коррутине запрос
    * дергается один за другим, тем самым данные могут подгружаться дольше.*/
    fun loadData() {
        liveData.value = AppState.Loading(0)

        viewModelScope.launch {
            val data = mutableListOf<DataModel>()

            try {
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
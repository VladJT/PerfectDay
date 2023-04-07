package jt.projects.perfectday.presentation.today

import jt.projects.model.AppState
import jt.projects.model.DataModel
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.utils.FACTS_COUNT
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

class TodayViewModel(
    private val birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    private val simpleNoticeInteractorImpl: SimpleNoticeInteractorImpl

) : BaseViewModel<AppState>() {

    private val currentDate = LocalDate.now()

    fun loadData() {
        liveData.value = AppState.Loading(0)

        viewModelCoroutineScope.launch {
            val data = mutableListOf<DataModel>()

            data.addAll(birthdayFromPhoneInteractor.getDataByDate(currentDate))
            liveData.value = AppState.Loading(50)

            data.addAll(simpleNoticeInteractorImpl.getFactsByDate(currentDate, FACTS_COUNT))
            liveData.value = AppState.Loading(100)

            handleResponse(AppState.Success(data))
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
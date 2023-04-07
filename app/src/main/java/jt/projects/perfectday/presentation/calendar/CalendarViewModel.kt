package jt.projects.perfectday.presentation.today

import jt.projects.model.AppState
import jt.projects.model.DataModel
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarViewModel(
    private val birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl

) : BaseViewModel<AppState>() {

    private val currentDate = LocalDate.now()

    fun loadData() {
        liveData.value = AppState.Loading(0)

        viewModelCoroutineScope.launch {
            val data = mutableListOf<DataModel>()

            data.addAll(birthdayFromPhoneInteractor.getAllData())
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
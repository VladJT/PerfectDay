package jt.projects.perfectday.core

import androidx.lifecycle.ViewModel

//Создадим базовую ViewModel, куда вынесем общий для всех функционал
abstract class BaseViewModel(

) : ViewModel() {

//    companion object {
//        const val FAKE_DELAY = 10L
//
//        const val PHONE_GROUP_LABEL = "Дни рождения контактов телефона"
//        const val VK_GROUP_LABEL = "Дни рождения друзей ВКонтакте"
//        const val SCHEDULED_EVENT_GROUP_LABEL = "Запланированные события"
//    }
//
//    protected val liveData: MutableLiveData<AppState> = MutableLiveData()
//    val liveDataForViewToObserve: LiveData<AppState>
//        get() {
//            return liveData
//        }
//
//    protected val data = mutableListOf<DataModel>()
//
//    fun loadData() {
//        liveData.value = AppState.Loading(20)
//        data.clear()
//
//        viewModelScope.launch {
//            try {
//                tryToExecute(::loadScheduledEvents, progress = 40, "Загружаем планы")
//                delay(FAKE_DELAY)
//
//                tryToExecute(::loadBirthdaysFromPhone, progress = 70, "Загружаем контакты телефона")
//                delay(FAKE_DELAY)
//
//                tryToExecute(::loadBirthdaysFromVk, progress = 100, "Загружаем контакты из VK.com")
//                delay(FAKE_DELAY)
//
//
//                preparePostValue()
//                liveData.postValue(AppState.Success(data))
//            } catch (e: CancellationException) {
//                throw e
//            } catch (e: Exception) {
//                liveData.postValue(AppState.Error(e))
//            }
//        }
//    }
//
//    private suspend fun tryToExecute(
//        method: KSuspendFunction0<Unit>,
//        progress: Int,
//        progressStatus: String?
//    ) {
//        try {
//            method.invoke()
//            liveData.value = AppState.Loading(progress, progressStatus)
//        } catch (e: Exception) {
//            handleError(e)
//        }
//    }
//
//    protected fun handleError(e: Exception) {
//        Log.d(LOG_TAG, e.message.toString())
//    }
//
//    protected fun addHeaderRow(name: String) {
//        data.add(DataModel.SimpleNotice(name, ""))
//    }
//
//    open fun preparePostValue() {}
//
//    abstract suspend fun loadBirthdaysFromPhone()
//
//    abstract suspend fun loadBirthdaysFromVk()
//
//    abstract suspend fun loadScheduledEvents()
//
//    override fun onCleared() {
//        liveData.value = AppState.Success(null)
//        super.onCleared()
//    }
//
//    fun deleteScheduledEvent(eventId: Int) {
//        viewModelScope.launch {
//            scheduledEventInteractor.deleteScheduledEventById(eventId)
//            data.removeAll { it is DataModel.ScheduledEvent && it.id == eventId }
//        }
//    }
}
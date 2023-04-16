package jt.projects.perfectday.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jt.projects.model.AppState
import jt.projects.model.DataModel
import jt.projects.utils.LOG_TAG
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

//Создадим базовую ViewModel, куда вынесем общий для всех функционал
abstract class BaseViewModel(
    protected val settingsPreferences: SimpleSettingsPreferences,
    protected val dataCache: AppDataCache
) : ViewModel() {

    companion object {
        const val FAKE_DELAY = 10L
    }

    protected val liveData: MutableLiveData<AppState> = MutableLiveData()
    val liveDataForViewToObserve: LiveData<AppState>
        get() {
            return liveData
        }

    protected val data = mutableListOf<DataModel>()

    fun loadData() {
        liveData.value = AppState.Loading(20)
        data.clear()

        viewModelScope.launch {
            try {
                tryToExecute(::loadScheduledEvents, progress = 40, "Загружаем планы")
                delay(FAKE_DELAY)

                tryToExecute(::loadBirthdaysFromPhone, progress = 70, "Загружаем контакты телефона")
                delay(FAKE_DELAY)

                tryToExecute(::loadBirthdaysFromVk, progress = 100, "Загружаем контакты из VK.com")
                delay(FAKE_DELAY)


                preparePostValue()
                liveData.postValue(AppState.Success(data))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                liveData.postValue(AppState.Error(e))
            }
        }
    }

    private suspend fun tryToExecute(
        method: KSuspendFunction0<Unit>,
        progress: Int,
        progressStatus: String?
    ) {
        try {
            method.invoke()
            liveData.value = AppState.Loading(progress, progressStatus)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    protected fun handleError(e: Exception) {
        Log.d(LOG_TAG, e.message.toString())
    }

    open fun preparePostValue() {}

    abstract suspend fun loadBirthdaysFromPhone()

    abstract suspend fun loadBirthdaysFromVk()

    abstract suspend fun loadScheduledEvents()

    override fun onCleared() {
        liveData.value = AppState.Success(null)
        super.onCleared()
    }

    fun deleteScheduledEvent(id: Int) {
        viewModelScope.launch {
            dataCache.deleteScheduledEventById(id)
            data.removeAll { it is DataModel.ScheduledEvent && it.id == id }
        }
    }
}
package jt.projects.perfectday.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jt.projects.model.AppState
import jt.projects.model.DataModel
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.utils.LOG_TAG
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.VK_AUTH_TOKEN
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

//Создадим базовую ViewModel, куда вынесем общий для всех функционал
abstract class BaseViewModel(
    protected val settingsPreferences: SimpleSettingsPreferences,
    protected val birthdayFromPhoneInteractor: BirthdayFromPhoneInteractorImpl,
    protected val simpleNoticeInteractorImpl: SimpleNoticeInteractorImpl,
    protected val getFriendsFromVkUseCase: GetFriendsFromVkUseCase,
    protected val scheduledEventInteractorImpl: ScheduledEventInteractorImpl
) : ViewModel() {
    protected val liveData: MutableLiveData<AppState> = MutableLiveData()
    val liveDataForViewToObserve: LiveData<AppState>
        get() {
            return liveData
        }

    private val vkToken: String? by lazy { settingsPreferences.getSettings(VK_AUTH_TOKEN) }
    protected val data = mutableListOf<DataModel>()

    fun loadData() {
        liveData.value = AppState.Loading(0)
        data.clear()

        viewModelScope.launch {
            try {
                tryToExecute(::loadBirthdaysFromPhone, progress = 20)
                tryToExecute(::loadBirthdaysFromVk, progress = 40)
                tryToExecute(::loadInterestingFacts, progress = 60)
                tryToExecute(::loadHolidays, progress = 80)
                tryToExecute(::loadScheduledEvents, progress = 100)

                liveData.postValue(AppState.Success(data))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                liveData.postValue(AppState.Error(e))
            }
        }
    }

    private suspend fun tryToExecute(method: KSuspendFunction0<Unit>, progress: Int) {
        try {
            method.invoke()
            liveData.value = AppState.Loading(progress)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    protected fun handleError(e: Exception) {
        Log.d(LOG_TAG, e.message.toString())
    }

    abstract suspend fun loadHolidays()

    abstract suspend fun loadInterestingFacts()

    abstract suspend fun loadBirthdaysFromPhone()

    abstract suspend fun loadBirthdaysFromVk()

    abstract suspend fun loadScheduledEvents()

    suspend fun loadFriendsFromVk(): List<DataModel> {
        if (vkToken == null || vkToken!!.isEmpty()) return emptyList()
        return getFriendsFromVkUseCase.getFriends(vkToken!!)
    }

    override fun onCleared() {
        liveData.value = AppState.Success(null)
        super.onCleared()
    }

    fun deleteScheduledEvent(id: Int) {
        viewModelScope.launch {
            scheduledEventInteractorImpl.deleteScheduledEventById(id)
            data.removeAll { it is DataModel.ScheduledEvent && it.id == id }
        }
    }
}
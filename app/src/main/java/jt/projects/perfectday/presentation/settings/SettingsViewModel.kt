package jt.projects.perfectday.presentation.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import jt.projects.perfectday.R
import jt.projects.perfectday.core.extensions.createMutableSingleEventFlow
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.repository.network.vk.VkNetworkRepository
import jt.projects.utils.extensions.emptyString
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.VK_AUTH_TOKEN
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class SettingsViewModel(
    private val settingsPref: SimpleSettingsPreferences,
    private val vkRepository: VkNetworkRepository,
    private val scheduledEventInteractor: ScheduledEventInteractorImpl
) : ViewModel() {
    private val _isLoadingProfile = MutableStateFlow(true)
    val isLoadingProfile get() = _isLoadingProfile.asStateFlow()

    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized get() = _isAuthorized.asStateFlow()

    private val _userInfo = MutableStateFlow(emptyString() to emptyString())
    val userInfo get() = _userInfo.asStateFlow()

    private val _errorFlow = createMutableSingleEventFlow<Int>()
    val errorFlow get() = _errorFlow.asSharedFlow()

    private val countOfDeletedEventsLiveData: MutableLiveData<Int> = MutableLiveData()
    val countOfDeletedEvents: LiveData<Int>
        get() {
            return countOfDeletedEventsLiveData
        }

    init {
        checkAuthorizedUser()
    }

    private fun checkAuthorizedUser() {
        _isLoadingProfile.tryEmit(false)
        val token = settingsPref.getSettings(VK_AUTH_TOKEN)
        val isAuthorizedUser = !(token == null || token.isEmpty())

        _isAuthorized.tryEmit(isAuthorizedUser)
        if (isAuthorizedUser) loadUserInfo(token!!)
        _isLoadingProfile.tryEmit(false)
    }

    private fun loadUserInfo(userToken: String) {
        viewModelScope.launch {
            try {
                val vkUserInfo = vkRepository.getUserInfo(userToken)
                val fullName = "${vkUserInfo.firstName} ${vkUserInfo.lastName}"
                _userInfo.tryEmit(fullName to vkUserInfo.photoMax)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "$e")
                _errorFlow.tryEmit(R.string.vk_error_auth_text)
            }
        }
    }

    fun checkVkResult(vkResult: VKAuthenticationResult) {
        when (vkResult) {
            is VKAuthenticationResult.Success -> {
                settingsPref.saveSettings(VK_AUTH_TOKEN, vkResult.token.accessToken)
                checkAuthorizedUser()
            }

            else ->
                _errorFlow.tryEmit(R.string.vk_error_auth_text)
        }
    }

    fun onClickButtonLogOut() {
        VK.logout()
        settingsPref.saveSettings(VK_AUTH_TOKEN, emptyString())
        _isAuthorized.tryEmit(false)
    }

    fun deleteOldScheduledEvents() {
        viewModelScope.launch {
            val date = LocalDate.now()
            val countToDelete = scheduledEventInteractor.getScheduledEventCountBeforeDate(date)
            scheduledEventInteractor.deleteScheduledEventBeforeDate(date)
            countOfDeletedEventsLiveData.postValue(countToDelete)
        }
    }
}
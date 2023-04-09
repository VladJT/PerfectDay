package jt.projects.perfectday.presentation.settings

import androidx.lifecycle.ViewModel
import com.vk.api.sdk.auth.VKAuthenticationResult
import jt.projects.perfectday.R
import jt.projects.perfectday.core.extensions.createMutableSingleEventFlow
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.VK_AUTH_TOKEN
import kotlinx.coroutines.flow.*

class SettingsViewModel(
    private val settingsPref: SimpleSettingsPreferences
) : ViewModel() {
    private val _isLoadingProfile = MutableStateFlow(true)
    val isLoadingProfile get() = _isLoadingProfile.asStateFlow()

    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized get() = _isAuthorized.asStateFlow()

    private val _errorFlow = createMutableSingleEventFlow<Int>()
    val errorFlow get() = _errorFlow.asSharedFlow()

    init {
        checkAuthorizedUser()
    }

    private fun checkAuthorizedUser() {
        val token = settingsPref.getSettings(VK_AUTH_TOKEN)
        val isAuthorizedUser = !(token == null || token.isEmpty())

        _isAuthorized.tryEmit(isAuthorizedUser)
        _isLoadingProfile.tryEmit(false)
    }

    fun checkVkResult(vkResult: VKAuthenticationResult) {
        when (vkResult) {
            is VKAuthenticationResult.Success ->
                settingsPref.saveSettings(VK_AUTH_TOKEN, vkResult.token.accessToken)
            else ->
                _errorFlow.tryEmit(R.string.vk_error_auth_text)
        }
    }
}
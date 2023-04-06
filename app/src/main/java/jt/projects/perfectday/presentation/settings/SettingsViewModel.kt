package jt.projects.perfectday.presentation.settings

import androidx.lifecycle.ViewModel
import com.vk.api.sdk.auth.VKAuthenticationResult
import jt.projects.perfectday.R
import jt.projects.perfectday.core.extensions.createMutableSingleEventFlow
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.VK_AUTH_TOKEN
import kotlinx.coroutines.flow.asSharedFlow

class SettingsViewModel(
    private val settingsPref: SimpleSettingsPreferences
): ViewModel() {
    private val _errorFlow = createMutableSingleEventFlow<Int>()
    val errorFlow get() = _errorFlow.asSharedFlow()

    fun checkVkResult(vkResult: VKAuthenticationResult) {
        when (vkResult) {
            is VKAuthenticationResult.Success ->
                settingsPref.saveSettings(VK_AUTH_TOKEN, vkResult.token.accessToken)
            else ->
                _errorFlow.tryEmit(R.string.vk_error_auth_text)
        }
    }
}
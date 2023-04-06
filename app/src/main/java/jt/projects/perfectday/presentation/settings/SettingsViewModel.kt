package jt.projects.perfectday.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import com.vk.api.sdk.auth.VKAuthenticationResult
import jt.projects.perfectday.R
import jt.projects.perfectday.core.extensions.createMutableSingleEventFlow
import kotlinx.coroutines.flow.asSharedFlow

class SettingsViewModel : ViewModel() {
    private val _errorFlow = createMutableSingleEventFlow<Int>()
    val errorFlow get() = _errorFlow.asSharedFlow()

    fun checkVkResult(vkResult: VKAuthenticationResult) {
        when (vkResult) {
            is VKAuthenticationResult.Success -> {
                Log.d("TAG", "res =${vkResult.token.accessToken}")
            }
            else -> _errorFlow.tryEmit(R.string.vk_error_auth_text)
        }
    }
}
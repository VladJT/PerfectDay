package jt.projects.perfectday.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import com.vk.api.sdk.auth.VKAuthenticationResult

class SettingsViewModel : ViewModel() {


    fun checkVkResult(vkResult: VKAuthenticationResult) {
        when (vkResult) {
            is VKAuthenticationResult.Success -> {
                Log.d("TAG", "res =${vkResult.token.accessToken}")
            }
            else -> {

            }
        }
    }
}
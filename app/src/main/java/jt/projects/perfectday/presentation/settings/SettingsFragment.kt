package jt.projects.perfectday.presentation.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import jt.projects.perfectday.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val launcherVk = registerForActivityResult(VK.getVKAuthActivityResultContract()) { result ->
        when (result) {
            is VKAuthenticationResult.Success -> {
                Log.d("TAG", "res =${result.token.accessToken}")
            }
            else -> {

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vkLogin.setOnClickListener {
            launcherVk.launch(listOf())
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
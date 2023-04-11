package jt.projects.perfectday.presentation.settings

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import coil.load
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import jt.projects.perfectday.databinding.FragmentSettingsBinding
import jt.projects.utils.showSnackbar
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()
    private val launcherVk =
        registerForActivityResult(VK.getVKAuthActivityResultContract()) { result ->
            viewModel.checkVkResult(result)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeError()
        setOnButtonsListener()
        observeVisibleProfile()
        observeUserInfo()
    }

    private fun observeError() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorFlow.collect(::displayError)
            }
        }
    }

    private fun displayError(@StringRes resource: Int) {
        val text = getString(resource)
        requireActivity().showSnackbar(text)
    }

    private fun setOnButtonsListener() {
        binding.tvVkLogin.setOnClickListener {
            launcherVk.launch(listOf(VKScope.FRIENDS))
        }

        binding.btnVkLogout.setOnClickListener {
            viewModel.onClickButtonLogOut()
        }
    }

    private fun observeVisibleProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoadingProfile
                    .combine(viewModel.isAuthorized) { isLoading, isAuthorized -> isLoading to isAuthorized }
                    .collect { (isLoading, isAuthorized) ->
                        setVisibleLoading(isLoading)
                        setVisibleAuthorized(isAuthorized)
                    }
            }
        }
    }

    private fun setVisibleLoading(isLoading: Boolean) {
        binding.pbLoadingProfile.isVisible = isLoading
    }

    private fun setVisibleAuthorized(isAuthorized: Boolean) {
        with(binding) {
            tvVkLogin.isVisible = !isAuthorized
            headerVkUserInfo.root.isVisible = isAuthorized
            btnVkLogout.isVisible = isAuthorized
        }
    }

    private fun observeUserInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userInfo.collect { (fullName, urlImage) ->
                    setUserInfo(fullName, urlImage)
                }
            }
        }
    }

    private fun setUserInfo(fullName: String, urlImage: String) {
        with(binding.headerVkUserInfo) {
            ivVkUser.load(urlImage)
            tvVkUserName.text = fullName
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
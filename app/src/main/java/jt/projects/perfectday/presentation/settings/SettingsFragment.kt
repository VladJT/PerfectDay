package jt.projects.perfectday.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.google.android.material.slider.Slider
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import jt.projects.perfectday.R
import jt.projects.perfectday.core.extensions.navigateToFragment
import jt.projects.perfectday.databinding.FragmentSettingsBinding
import jt.projects.utils.REMINDER_PERIOD_KEY
import jt.projects.utils.extensions.showSnackbar
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.toStdFormatString
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsPreferences by inject<SimpleSettingsPreferences>()

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
        initDeleteOldScheduledEvents()
        observeVisibleProfile()
        observeUserInfo()
        initDaySliderForReminderFragment()
        initPushSettings()
    }

    private fun initPushSettings() {
        binding.textViewPushsetting.setOnClickListener {
            navigateToFragment(PushSettingFragment.newInstance(), true)
        }
    }

    private fun initDeleteOldScheduledEvents() {
        binding.btnDeleteOldSheduledEvents.setOnClickListener {
            viewModel.deleteOldScheduledEvents()
        }

        viewModel.countOfDeletedEvents.observe(viewLifecycleOwner) {
            if (it == 0)
                showSnackbar(getString(R.string.no_data_to_delete))
            else
                showSnackbar(getString(R.string.deleted) + "$it" + getString(R.string.last_events))
        }
    }

    private fun initDaySliderForReminderFragment() {
        binding.daysSlider.value = settingsPreferences.getDaysPeriodForReminderFragment().toFloat()
        setSliderValueLabel(binding.daysSlider.value)

        binding.daysSlider.addOnChangeListener { slider, value, fromUser ->
            setSliderValueLabel(slider.value)
        }

        binding.daysSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                saveSettings()
            }
        })
    }

    private fun setSliderValueLabel(value: Float) {
        val startDate = LocalDate.now()
        val endDate = startDate.plusDays(value.toLong())
        binding.tvSliderValue.text =
            "${value.toInt()} " + getString(R.string.Days) + " (${startDate.toStdFormatString()} - ${endDate.toStdFormatString()})"
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
        showSnackbar(text)
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

    private fun saveSettings() {
        settingsPreferences.saveInt(
            REMINDER_PERIOD_KEY,
            binding.daysSlider.value.toInt()
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
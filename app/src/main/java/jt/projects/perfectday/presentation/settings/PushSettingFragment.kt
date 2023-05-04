package jt.projects.perfectday.presentation.settings

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.FragmentPushSettingBinding
import jt.projects.perfectday.push.PushManager
import jt.projects.utils.DEBUG
import jt.projects.utils.LOG_TAG
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PushSettingFragment : Fragment() {

    companion object {
        fun newInstance(): PushSettingFragment = PushSettingFragment()
    }

    private var _binding: FragmentPushSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PushSettingViewModel by viewModel()
    private val pushManager by inject<PushManager>()
    private var statusPush = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPushSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwitch()
        observeVisibleDataPicker()
        observeHourData()
        observeMinuteData()

        initDataPicker()
    }

    private fun initDataPicker() {
        binding.timepickerPush.setOnTimeChangedListener { _, hourOfDay, minute ->
            viewModel.onSelectHourData(hourOfDay)
            viewModel.onSelectMinuteData(minute)
            if (DEBUG) {
                Log.d(LOG_TAG, "hour DataPicker: $hourOfDay")
                Log.d(LOG_TAG, "minute DataPicker: $minute")
            }
        }
    }

    private fun setVisibleDataPicker(isOnOffPush: Boolean) {
        binding.timepickerPush.visibility = if (isOnOffPush) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun initSwitch() {
        binding.switchOnpush.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onClickButtonSwitchOnOffPush(isChecked)
        }
    }

    private fun observeVisibleDataPicker() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isOnPushService.collect {
                setSwitchChecked(it)
                setVisibleDataPicker(it)
                setTextSwitchHead(it)
                setVisibleDescription(it)
            }
        }
    }

    private fun setVisibleDescription(checkedSwitch: Boolean) {
        if (checkedSwitch) {
            binding.textViewHelp.visibility = View.GONE
        } else {
            binding.textViewHelp.visibility = View.VISIBLE
        }
    }

    private fun setTextSwitchHead(checkedSwitch: Boolean) {
        if (checkedSwitch) {
            binding.switchOnpush.text = getString(R.string.head_text_button_switch_on)
        } else {
            binding.switchOnpush.text = getString(R.string.head_text_button_switch_off)
        }
    }

    private fun setSwitchChecked(checkedSwitch: Boolean) {
        binding.switchOnpush.isChecked = checkedSwitch
        statusPush = checkedSwitch
        viewModel.saveTimeToSharedPreferences()

        if (checkedSwitch) {
            // проверка на разрешения отправлять оповещения
            if (!checkPermissionPostNotifications()) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.check_permissions_notification),
                    Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.open)) {
                        openNotificationsSettings()
                    }.show()
            } else {
                // проверка на автозапуск приложения (для отправки оповещений когда приложение не запущено)
                if (!checkPermissionAutoStartApplication()) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.check_permissions_autostart),
                        Snackbar.LENGTH_LONG
                    ).setAction(getString(R.string.open)) {
                            openAutoStartSettings()
                        }.show()
                }
            }
        }

    }

    private fun observeHourData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hourTime.collect {
                binding.timepickerPush.hour = it
            }
        }
    }

    private fun observeMinuteData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.minuteTime.collect {
                binding.timepickerPush.minute = it
            }
        }
    }


    private fun checkWorkManager() {
        try {
            if (statusPush && checkPermissionPostNotifications()) {
                pushManager.stopWork()
                pushManager.startWork()
            } else {
                pushManager.stopWork()
            }

        } catch (e: Exception) {
            Log.d(LOG_TAG, e.message.toString())
        }
    }


    private fun checkPermissionPostNotifications(): Boolean = (ActivityCompat.checkSelfPermission(
        requireContext(), Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED)

    private fun checkPermissionAutoStartApplication(): Boolean =
        (ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.RECEIVE_BOOT_COMPLETED
        ) == PackageManager.PERMISSION_GRANTED)

    private fun openAutoStartSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_SETTINGS
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("android.provider.extra.APP_PACKAGE", requireContext().packageName)
        requireContext().startActivity(intent)
    }

    private fun openNotificationsSettings() {
        val intent = Intent()
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("android.provider.extra.APP_PACKAGE", requireContext().packageName)
        requireContext().startActivity(intent)
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.saveTimeToSharedPreferences()
        checkWorkManager()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
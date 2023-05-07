package jt.projects.perfectday.presentation.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.FragmentPushSettingBinding
import jt.projects.perfectday.push.PushManager
import jt.projects.utils.DEBUG
import jt.projects.utils.LOG_TAG
import jt.projects.utils.REQUEST_CODE_POST_NOTIFICATIONS
import jt.projects.utils.REQUEST_CODE_RECEIVE_BOOT_COMPLETED
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
            checkPermissionPostNotification()
            // проверка на автозапуск приложения (для отправки оповещений когда приложение не запущено)
            checkPermissionAutoStartApplication()
        }
    }


    private fun checkPermissionPostNotification(): Boolean {
        val permResult =
            (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED)

        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            AlertDialog.Builder(
                ContextThemeWrapper(
                    requireContext(),
                    R.style.PerfectDay_MaterialCalendarTheme
                )
            )
                .setTitle(getString(R.string.alert_notification_title))
                .setMessage(getString(R.string.alert_notification_message))
                .setPositiveButton(getString(R.string.open_permission_settings)) { _, _ ->
                    permissionRequest(
                        Manifest.permission.POST_NOTIFICATIONS,
                        REQUEST_CODE_POST_NOTIFICATIONS
                    )
                }
                .setNegativeButton(getString(R.string.close_permission_settings)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        } else {
            permissionRequest(
                Manifest.permission.POST_NOTIFICATIONS,
                REQUEST_CODE_POST_NOTIFICATIONS
            )
        }
        return permResult
    }

    private fun checkPermissionAutoStartApplication(): Boolean {
        val permResult =
            (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECEIVE_BOOT_COMPLETED
            ) == PackageManager.PERMISSION_GRANTED)

        if (shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_BOOT_COMPLETED)) {
            AlertDialog.Builder(
                ContextThemeWrapper(
                    requireContext(),
                    R.style.PerfectDay_MaterialCalendarTheme
                )
            )
                .setTitle(getString(R.string.alert_autostart_title))
                .setMessage(getString(R.string.alert_autostart_message))
                .setPositiveButton(getString(R.string.open_permission_settings)) { _, _ ->
                    permissionRequest(
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        REQUEST_CODE_RECEIVE_BOOT_COMPLETED
                    )
                }
                .setNegativeButton(getString(R.string.close_permission_settings)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        } else {
            permissionRequest(
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                REQUEST_CODE_RECEIVE_BOOT_COMPLETED
            )
        }
        return permResult
    }

    private fun permissionRequest(permission: String, requestCode: Int) {
        requestPermissions(arrayOf(permission), requestCode)
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
            if (statusPush && checkPermissionPostNotification()) {
                pushManager.stopWork()
                pushManager.startWork()
            } else {
                pushManager.stopWork()
            }

        } catch (e: Exception) {
            Log.d(LOG_TAG, e.message.toString())
        }
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
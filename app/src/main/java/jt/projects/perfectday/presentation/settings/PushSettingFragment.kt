package jt.projects.perfectday.presentation.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.FragmentPushSettingBinding
import jt.projects.perfectday.push.PushManager
import jt.projects.utils.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.Koin

class PushSettingFragment : Fragment() {

    private var _binding: FragmentPushSettingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PushSettingViewModel by viewModel()

    private val pushM by inject<PushManager>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwitch()
        initDataPicker()
        observeVisibleDataPicker()
        observeHourData()
        observeMinuteData()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPushSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(): PushSettingFragment = PushSettingFragment()
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

    private fun setSwitchChecked(chekedSwitch: Boolean) {
        binding.switchOnpush.isChecked = chekedSwitch
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

    override fun onDetach() {
        super.onDetach()
        startWorkManager()

    }

    private fun startWorkManager() {
//        val pushM:PushManager = Koin().get()

//        requireActivity().getSharedPreferences(PUSH_PARAM, Context.MODE_PRIVATE).let {
//            val startPush = it.getBoolean(PUSH_START, false)
//            if (startPush) {
//                pushM.startWork()
//            } else {
//                pushM.stopWork()
//            }
//        }
    }
}
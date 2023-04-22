package jt.projects.perfectday.presentation.schedule_event

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.*
import com.google.android.material.datepicker.MaterialDatePicker
import jt.projects.perfectday.databinding.FragmentScheduleEventBinding
import jt.projects.utils.*
import jt.projects.utils.extensions.emptyString
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.*

private const val DATE_PICKER_TAG = "DATE_PICKER_TAG"

class ScheduleEventDialogFragment : DialogFragment() {
    private var _binding: FragmentScheduleEventBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleEventViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDateInButton()
        setButtonChooseDateListener()
        setSaveButtonClick()
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun setDateInButton() {
        binding.btnChooseDate.text = arguments?.getString(DATE_STRING_KEY) ?: emptyString()
    }

    private fun setButtonChooseDateListener() {
        binding.btnChooseDate.setOnClickListener {
            MaterialDatePicker.Builder
                .datePicker()
                .build()
                .apply {
                    addOnPositiveButtonClickListener {
                        val date =
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .toStdFormatString()
                        binding.btnChooseDate.text = date
                    }
                }
                .show(requireActivity().supportFragmentManager, DATE_PICKER_TAG)
        }
    }

    private fun setSaveButtonClick() {
        with(binding) {
            btnSave.setOnClickListener {
                val headerNote = scheduledEventHeader.text.toString()
                val description = scheduledEventDescription.text.toString()
                val date = btnChooseDate.text.toString()

            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isCloseFragment.collect { dismiss() }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val DATE_STRING_KEY = "date_key"

        fun newInstance(date: String): ScheduleEventDialogFragment =
            ScheduleEventDialogFragment().apply {
                arguments = bundleOf(DATE_STRING_KEY to date)
            }
    }
}
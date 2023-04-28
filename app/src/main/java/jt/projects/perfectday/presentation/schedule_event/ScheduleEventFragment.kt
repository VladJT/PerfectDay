package jt.projects.perfectday.presentation.schedule_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.datepicker.MaterialDatePicker
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.core.extensions.showButtonBackHome
import jt.projects.perfectday.core.extensions.showFab
import jt.projects.perfectday.databinding.FragmentScheduleEventBinding
import jt.projects.utils.extensions.emptyString
import jt.projects.utils.toStdFormatString
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.time.ZoneId

private const val DATE_STRING_KEY = "date_key"
private const val ID_NOTE_KEY = "id_note_key"
private const val DATE_PICKER_TAG = "DATE_PICKER_TAG"

class ScheduleEventFragment : Fragment() {
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
        super.onViewCreated(view, savedInstanceState)
        showButtonBackHome(true)
        showFab(false)
        observeScheduleEvent()
        setSaveButtonClick()
        setOnClickButtonChooseDate()

        binding.btnChooseDate.text = arguments?.getString(DATE_STRING_KEY) ?: emptyString()
    }

    private fun observeScheduleEvent() {
        viewModel.getNote(arguments?.getInt(ID_NOTE_KEY, -1))
        viewModel.note.observe(viewLifecycleOwner, ::renderData)
    }

    private fun renderData(data: DataModel.ScheduledEvent) {
        with(binding) {
            btnChooseDate.text = data.date.toStdFormatString()
            scheduledEventHeader.setText(data.name)
            scheduledEventDescription.setText(data.description)
        }
    }

    private fun setSaveButtonClick() {
        with(binding) {
            btnSave.setOnClickListener {
                val headerNote = scheduledEventHeader.text.toString()
                val description = scheduledEventDescription.text.toString()
                val date = btnChooseDate.text.toString()
                viewModel.saveOrUpdateNote(headerNote, description, date)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isCloseFragment.collect { parentFragmentManager.popBackStack() }
            }
        }
    }

    private fun setOnClickButtonChooseDate() {
        binding.btnChooseDate.setOnClickListener {
            MaterialDatePicker.Builder
                .datePicker()
                .setTheme(R.style.PerfectDay_MaterialCalendarTheme)
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

    override fun onDestroyView() {
        showButtonBackHome(false)
        showFab(true)
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(date: String): ScheduleEventFragment =
            ScheduleEventFragment().apply {
                arguments = bundleOf(DATE_STRING_KEY to date)
            }

        fun newInstance(id: Int): ScheduleEventFragment =
            ScheduleEventFragment().apply {
                arguments = bundleOf(ID_NOTE_KEY to id)
            }
    }
}
package jt.projects.perfectday.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.DialogScheduleEventBinding
import jt.projects.repository.room.LocalRepository
import jt.projects.utils.showToast
import jt.projects.utils.toStdFormatString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class ScheduleEventDialogFragment() : AppCompatDialogFragment() {
    private var _binding: DialogScheduleEventBinding? = null
    private val binding get() = _binding!!

    private var scheduledEvent: DataModel.ScheduledEvent? = null
    private val interactor by inject<LocalRepository>()

    companion object {
        const val TAG = "ScheduleEventDialogFragment"
        const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
        private const val BUNDLE_KEY = "BUNDLE_KEY"

        fun newInstance(data: DataModel.ScheduledEvent?): ScheduleEventDialogFragment {
            val dialogFragment = ScheduleEventDialogFragment()
            val args = Bundle()
            args.putParcelable(BUNDLE_KEY, data)
            dialogFragment.arguments = args
            return dialogFragment
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogScheduleEventBinding.inflate(requireActivity().layoutInflater, null, false)

        setDataFromBundle()
        setButtonChooseDateListener()
        setButtonSaveListener()
        setButtonCloseListener()

        return AlertDialog
            .Builder(requireContext())
            .setTitle(getString(R.string.My_notice))
            .setView(binding.root)
            .setIcon(android.R.drawable.ic_input_get)
            .setCancelable(false)
            .create()
    }

    private fun getDataFromBundle(): DataModel.ScheduledEvent? =
        arguments?.getParcelable(BUNDLE_KEY) as? DataModel.ScheduledEvent

    private fun setDataFromBundle() {
        scheduledEvent = getDataFromBundle()
        if (scheduledEvent == null) {
            scheduledEvent = DataModel.ScheduledEvent(-1, "", LocalDate.now(), "")
        }
        renderData()
    }

    private fun renderData() {
        scheduledEvent?.let { data ->
            binding.btnChooseDate.text = data.date.toStdFormatString()
            binding.scheduledEventHeader.setText(data.name)
            binding.scheduledEventDescription.setText(data.description)
        }
    }

    private fun setButtonChooseDateListener() {
        binding.btnChooseDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder
                .datePicker().build()
            datePicker.show(requireActivity().supportFragmentManager, DATE_PICKER_TAG)

            datePicker.addOnPositiveButtonClickListener {
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
                val date = dateFormatter.format(Date(it))
                scheduledEvent?.date = LocalDate.parse(date)
                renderData()
            }
        }
    }


    private fun setButtonSaveListener() {
        binding.btnSave.setOnClickListener {
            try {

                CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
                    scheduledEvent?.let {
                        if (it.id == -1) {
                            interactor.insert(it)
                        } else {
                            interactor.update(it)
                        }
                    }
                }
            } catch (e: Exception) {
                requireActivity().showToast(e.message.toString())
            }
            this.dismiss()
        }
    }

    private fun setButtonCloseListener() {
        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
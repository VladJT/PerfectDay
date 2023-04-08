package jt.projects.perfectday.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.databinding.DialogScheduleEventBinding
import jt.projects.perfectday.databinding.FragmentSettingsBinding


class ScheduleEventDialogFragment() : AppCompatDialogFragment() {

    companion object {
        const val TAG = "ScheduleEventDialogFragment"
        private const val BUNDLE_KEY = "BUNDLE_KEY"

        fun newInstance(data: DataModel.ScheduledEvent?): ScheduleEventDialogFragment {
            val dialogFragment = ScheduleEventDialogFragment()
            val args = Bundle()
            args.putParcelable(BUNDLE_KEY, data)
            dialogFragment.arguments = args
            return dialogFragment
        }
    }

    private var _binding: DialogScheduleEventBinding? = null
    private val binding get() = _binding!!


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogScheduleEventBinding.inflate(requireActivity().layoutInflater, null, false)

        setDataFromBundle()

        return AlertDialog
            .Builder(requireContext())
            .setTitle(getString(R.string.My_notice))
            .setView(binding.root)
            .setIcon(R.drawable.baseline_notification_add_24)
            .setCancelable(false).create()
    }


    private fun getDataFromBundle(): DataModel.ScheduledEvent?{
        val args = arguments?.getParcelable(BUNDLE_KEY) as? DataModel.ScheduledEvent
        return args
    }

    private fun setDataFromBundle() {
        getDataFromBundle()?.let { data ->
            binding.currentDate.text = data.date.toString()
            binding.scheduledEventHeader.setText(data.name)
            binding.scheduledEventDescription.setText(data.description)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
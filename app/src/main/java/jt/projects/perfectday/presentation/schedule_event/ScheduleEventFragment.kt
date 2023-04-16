package jt.projects.perfectday.presentation.schedule_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import jt.projects.model.DataModel
import jt.projects.perfectday.core.extensions.showButtonBackHome
import jt.projects.perfectday.core.extensions.showFab
import jt.projects.perfectday.databinding.FragmentScheduleEventBinding
import jt.projects.utils.showToast
import jt.projects.utils.toStdFormatString
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class ScheduleEventFragment() : Fragment() {
    private var _binding: FragmentScheduleEventBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScheduleEventViewModel by viewModel()

    companion object {
        const val TAG = "ScheduleEventDialogFragment"
        const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
        private const val BUNDLE_KEY = "BUNDLE_KEY"

        fun newInstance(data: DataModel.ScheduledEvent?): ScheduleEventFragment {
            val args = Bundle()
            val fragment = ScheduleEventFragment()
            args.putParcelable(BUNDLE_KEY, data)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleEventBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        showButtonBackHome(true)
        showFab(false)
        setButtonChooseDateListener()
        setButtonSaveListener()

    }

    private fun getDataFromBundle(): DataModel.ScheduledEvent? =
        arguments?.getParcelable(BUNDLE_KEY) as? DataModel.ScheduledEvent

    private fun initViewModel() {
        viewModel.liveDataForViewToObserve.observe(this@ScheduleEventFragment) {
            renderData(it)
        }

        var data = getDataFromBundle()
        if (data == null) {
            data = DataModel.ScheduledEvent(
                id = 0,
                name = "",
                date = LocalDate.now(),
                description = ""
            )
        }
        viewModel.setData(data)
    }

    private fun renderData(data: DataModel.ScheduledEvent) {
        binding.btnChooseDate.text = data.date.toStdFormatString()
        binding.scheduledEventHeader.setText(data.name)
        binding.scheduledEventDescription.setText(data.description)
    }

    private fun setButtonChooseDateListener() {
        binding.btnChooseDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder
                .datePicker().build()
            datePicker.show(requireActivity().supportFragmentManager, DATE_PICKER_TAG)

            datePicker.addOnPositiveButtonClickListener {
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
                val date = dateFormatter.format(Date(it))
                val newDate = LocalDate.parse(date)
                viewModel.updateData(
                    date = newDate,
                    name = binding.scheduledEventHeader.text.toString(),
                    description = binding.scheduledEventDescription.text.toString()
                )
            }
        }
    }

    private fun setButtonSaveListener() {
        binding.btnSave.setOnClickListener {
            try {
                viewModel.updateData(
                    name = binding.scheduledEventHeader.text.toString(),
                    description = binding.scheduledEventDescription.text.toString()
                )
                viewModel.saveData()

            } catch (e: Exception) {
                requireActivity().showToast(e.message.toString())
            }
            requireActivity().supportFragmentManager.popBackStack()//EXIT
        }
    }

    override fun onDestroyView() {
        showButtonBackHome(false)
        showFab(true)
        _binding = null
        super.onDestroyView()
    }
}
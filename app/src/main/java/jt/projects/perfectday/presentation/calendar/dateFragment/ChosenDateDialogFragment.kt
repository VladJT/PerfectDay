package jt.projects.perfectday.presentation.calendar.dateFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.core.BaseAdapter
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.core.extensions.editScheduledEvent
import jt.projects.perfectday.databinding.ChosenDateDialogFragmentBinding
import jt.projects.perfectday.presentation.calendar.DateIndicator
import jt.projects.utils.chosenCalendarDate
import kotlinx.coroutines.launch
import ru.cleverpumpkin.calendar.CalendarDate
import java.time.LocalDate

class ChosenDateDialogFragment(date: CalendarDate, calendarViewModel: BaseViewModel) :
    DialogFragment() {

    private var viewModel = calendarViewModel

    private val chosenDate: LocalDate = LocalDate.of(date.year, date.month + 1, date.dayOfMonth)

    private var _binding: ChosenDateDialogFragmentBinding? = null
    private val binding get() = _binding!!

    private val chosenDateAdapter: BaseAdapter by lazy {
        BaseAdapter(
            viewModel::onEditNoteClicked,
            viewModel::onDeleteNoteClicked,
            viewModel::onItemClicked
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        chosenCalendarDate = chosenDate
        _binding = ChosenDateDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRecyclerView()
        observeLoadingVisible()
        observeEditNote()

        viewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                dismiss()
            }
        })
    }

    private fun initViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultRecycler.collect { data ->
                    val chosenDateData = getDataByDate(data)
                    chosenDateAdapter.setData(chosenDateData)
                }
            }
        }
    }

    private fun getDataByDate(data: List<DataModel>): List<DataModel> {
        val returnList = mutableListOf<DataModel>()
        for (index in data.indices) {
            when (data[index]) {

                is DataModel.BirthdayFromPhone -> {
                    val birthdayData = data[index] as DataModel.BirthdayFromPhone
                    if (birthdayData.birthDate.monthValue == chosenDate.monthValue &&
                        birthdayData.birthDate.dayOfMonth == chosenDate.dayOfMonth
                    ) {
                        returnList.add(data[index])
                    }
                }

                is DataModel.BirthdayFromVk -> {
                    val birthdayData = data[index] as DataModel.BirthdayFromVk
                    if (birthdayData.birthDate.monthValue == chosenDate.monthValue &&
                        birthdayData.birthDate.dayOfMonth == chosenDate.dayOfMonth
                    ) {
                        returnList.add(data[index])
                    }
                }

                is DataModel.ScheduledEvent -> {
                    val eventData = data[index] as DataModel.ScheduledEvent
                    if (eventData.date.monthValue == chosenDate.monthValue &&
                        eventData.date.dayOfMonth == chosenDate.dayOfMonth
                    ) {
                        returnList.add(data[index])
                    }
                }

                else -> {}
            }
        }
        return returnList
    }

    private fun initRecyclerView() {
        with(binding.chosenDateRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chosenDateAdapter
        }
    }


    private fun observeLoadingVisible() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    binding.loadingFrameLayout.isVisible = it
                }
            }
        }
    }

    private fun observeEditNote() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.noteIdFlow.collect(::editScheduledEvent)
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}

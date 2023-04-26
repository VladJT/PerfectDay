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
import jt.projects.perfectday.core.BaseAdapter
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.core.extensions.editScheduledEvent
import jt.projects.perfectday.databinding.ChosenDateDialogFragmentBinding
import jt.projects.utils.VM_CALENDAR
import jt.projects.utils.VM_CHOSEN_DATE
import jt.projects.utils.chosenCalendarDate
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import ru.cleverpumpkin.calendar.CalendarDate
import java.time.LocalDate

class ChosenDateDialogFragment(date: CalendarDate) : DialogFragment() {

    private val chosenDate: LocalDate = LocalDate.of(date.year, date.month + 1, date.dayOfMonth)

    private var _binding: ChosenDateDialogFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BaseViewModel by viewModel(named(VM_CHOSEN_DATE))

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
                    chosenDateAdapter.setData(data)
                }
            }
        }
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
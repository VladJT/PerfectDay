package jt.projects.perfectday.presentation.calendar.dateFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import jt.projects.model.AppState
import jt.projects.model.DataModel
import jt.projects.perfectday.core.BaseAdapter
import jt.projects.perfectday.core.extensions.showProgress
import jt.projects.perfectday.core.extensions.showScheduledEvent
import jt.projects.perfectday.databinding.ChosenDateDialogFragmentBinding
import jt.projects.utils.chosenCalendarDate
import jt.projects.utils.showSnackbar
import jt.projects.utils.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.cleverpumpkin.calendar.CalendarDate
import java.time.LocalDate

class ChosenDateDialogFragment(date: CalendarDate) : DialogFragment() {

    private val chosenDate:LocalDate = LocalDate.of(date.year, date.month + 1, date.dayOfMonth)

    private var _binding: ChosenDateDialogFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChosenDateViewModel by viewModel()

    private val chosenDateAdapter: BaseAdapter by lazy { BaseAdapter(::onItemClick, ::onItemDelete) }

    private fun onItemClick(data: DataModel) {
        if (data is DataModel.ScheduledEvent) {
            showScheduledEvent(data)
            dismiss()
        } else {
            requireActivity().showToast(data.toString())
        }
    }

    private fun onItemDelete(data: DataModel, position: Int) {
        if (data is DataModel.ScheduledEvent) {
            viewModel.deleteScheduledEvent(data.id)
            chosenDateAdapter.notifyItemRemoved(position)
        }
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
        dialog?.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRecyclerView()
    }

    private fun initViewModel() {
        viewModel.liveDataForViewToObserve.observe(this@ChosenDateDialogFragment) {
            renderData(it)
        }
        viewModel.loadData()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                showLoadingFrame(false)
                appState.data?.let { data ->
                    chosenDateAdapter.setData(data)
                }
            }
            is AppState.Loading -> {
                showLoadingFrame(true)
                appState.progress?.let { showProgress(it) }
            }
            is AppState.Error -> {
                showLoadingFrame(false)
                showSnackbar(appState.error.message.toString())
            }
        }
    }


    private fun initRecyclerView() {
        with(binding.chosenDateRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chosenDateAdapter
        }
    }

    private fun showLoadingFrame(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingFrameLayout.visibility = View.VISIBLE
        } else {
            binding.loadingFrameLayout.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
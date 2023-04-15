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
import jt.projects.perfectday.core.extensions.showProgress
import jt.projects.perfectday.databinding.ChosenDateDialogFragmentBinding
import jt.projects.utils.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.cleverpumpkin.calendar.CalendarDate

class ChosenDateDialogFragment(date: CalendarDate) : DialogFragment() {

    private val chosenDate = date

    private var _binding: ChosenDateDialogFragmentBinding? = null
    private val binding get() = _binding!!

    private val chosenDateAdapter: ChosenDateAdapter by lazy { ChosenDateAdapter() }

    private val viewModel: ChosenDateViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChosenDateDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCalendarViewModel()
        initRecyclerView()
    }

    private fun initCalendarViewModel() {
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
                    chosenDateAdapter.setData(initChosenDayList(data))
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

    private fun initChosenDayList(data: List<DataModel>): List<DataModel> {
        val chosenDateList: MutableList<DataModel> = mutableListOf()
        for (index in data.indices) {
            when (data[index]) {
                is DataModel.BirthdayFromPhone -> {
                    val birthdayData = data[index] as DataModel.BirthdayFromPhone
                    if (
                        birthdayData.birthDate.dayOfMonth.equals(chosenDate.dayOfMonth) &&
                        (birthdayData.birthDate.monthValue - 1).equals(chosenDate.month)
                    ) {
                        chosenDateList.add(data[index])
                    }
                }
                else -> {}
            }
        }
        return chosenDateList
    }

    private fun initRecyclerView() {
        with(binding.dateOnCalendarRecyclerView) {
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
}
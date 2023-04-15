package jt.projects.perfectday.presentation.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jt.projects.model.AppState
import jt.projects.model.DataModel
import jt.projects.perfectday.core.BaseAdapter
import jt.projects.perfectday.core.showProgress
import jt.projects.perfectday.core.showScheduledEvent
import jt.projects.perfectday.databinding.FragmentReminderBinding
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.showSnackbar
import jt.projects.utils.showToast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderFragment : Fragment() {
    private var _binding: FragmentReminderBinding? = null
    private val binding get() = _binding!!
    private val settingsPreferences by inject<SimpleSettingsPreferences>()

    companion object {
        fun newInstance() = ReminderFragment()
    }

    private val viewModel: ReminderViewModel by viewModel() // НЕ привязана к жизненному циклу Activity

    private val reminderAdapter: BaseAdapter by lazy { BaseAdapter(::onItemClick, ::onItemDelete) }

    private fun onItemClick(data: DataModel) {
        if (data is DataModel.ScheduledEvent) {
            showScheduledEvent(data)
        } else {
            requireActivity().showToast(data.toString())
        }
    }

    private fun onItemDelete(data: DataModel, position: Int) {
        if (data is DataModel.ScheduledEvent) {
            viewModel.deleteScheduledEvent(data.id)
            reminderAdapter.notifyItemRemoved(position)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initToggleButtons()
        initViewModel()
        initRecView()
    }

    private fun initToggleButtons() {
        binding.buttonTomorrow.setOnClickListener {
            viewModel.isShowTomorrow = true
            viewModel.loadData()
        }

        binding.buttonAllTime.text =
            "${settingsPreferences.getDaysPeriodForReminderFragment()} Дней"

        binding.buttonAllTime.setOnClickListener {
            viewModel.isShowTomorrow = false
            viewModel.loadData()
        }
    }

    private fun initViewModel() {
        viewModel.liveDataForViewToObserve.observe(this@ReminderFragment) {
            renderData(it)
        }

        viewModel.loadData()
    }

    private fun initRecView() {
        with(binding.reminderRecyclerview) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reminderAdapter
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                showLoadingFrame(false)
                val data = appState.data?.let { data ->
                    reminderAdapter.setData(data)
                }
            }
            is AppState.Loading -> {
                showLoadingFrame(true)
                appState.progress?.let { showProgress(it, appState.status) }
            }
            is AppState.Error -> {
                showLoadingFrame(false)
                showSnackbar(appState.error.message.toString())
            }
        }
    }

    private fun showLoadingFrame(isLoading: Boolean) {
        binding.loadingFrameLayout.isVisible = isLoading
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
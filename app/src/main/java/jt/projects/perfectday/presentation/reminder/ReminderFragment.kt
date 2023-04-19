package jt.projects.perfectday.presentation.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import jt.projects.perfectday.core.BaseAdapter
import jt.projects.perfectday.core.extensions.showScheduledEvent
import jt.projects.perfectday.databinding.FragmentReminderBinding
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ReminderFragment : Fragment() {

    private var _binding: FragmentReminderBinding? = null
    private val binding get() = _binding!!
    private val settingsPreferences by inject<SimpleSettingsPreferences>()

    companion object {
        fun newInstance() = ReminderFragment()
    }

    private val viewModel: ReminderViewModel by activityViewModel()
    private val reminderAdapter by lazy {
        BaseAdapter(
            viewModel::onEditNoteClicked,
            viewModel::onDeleteNoteClicked
        )
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
        initRecView()
        observeEditNote()
        setSwipeToRefreshMove()
        setLoadingVisible()
    }

    private fun setSwipeToRefreshMove() {
//        binding.swipeToRefresh.setOnRefreshListener {
//            viewModel.onSwipeToRefreshMove()
//            binding.swipeToRefresh.isRefreshing = false
//        }
    }

    private fun initToggleButtons() {
        binding.buttonTomorrow.setOnClickListener {
            viewModel.isShowTomorrow = true
            viewModel.refreshData()
        }

        binding.buttonAllTime.text =
            "${settingsPreferences.getDaysPeriodForReminderFragment()} Дней"

        binding.buttonAllTime.setOnClickListener {
            viewModel.isShowTomorrow = false
            viewModel.refreshData()
        }
    }

    private fun initRecView() {
        with(binding.reminderRecyclerview) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reminderAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultRecycler.collect {
                    reminderAdapter.setData(it)
                }
            }
        }
    }


    private fun setLoadingVisible() {
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
                viewModel.noteFlow.collect(::showScheduledEvent)
            }
        }
    }


//    private fun renderData(appState: AppState) {
//        when (appState) {
//            is AppState.Success -> {
//                showLoadingFrame(false)
//                val data = appState.data?.let { data ->
//                    baseAdapter.setData(data)
//                }
//            }
//            is AppState.Loading -> {
//                showLoadingFrame(true)
//                appState.progress?.let { showProgress(it, appState.status) }
//            }
//            is AppState.Error -> {
//                showLoadingFrame(false)
//                showSnackbar(appState.error.message.toString())
//            }
//        }
//    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
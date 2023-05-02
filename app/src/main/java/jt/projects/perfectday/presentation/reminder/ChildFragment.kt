package jt.projects.perfectday.presentation.reminder

import android.os.Bundle
import android.util.Log
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
import jt.projects.perfectday.core.GlobalViewModel
import jt.projects.perfectday.core.extensions.editScheduledEvent
import jt.projects.perfectday.databinding.FragmentReminderChildBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import java.time.LocalDate

class ChildFragment(
    private val startDate: () -> LocalDate,
    private val endDate: () -> LocalDate
) : Fragment() {
    private var _binding: FragmentReminderChildBinding? = null
    private val binding get() = _binding!!

    private val viewModel = getKoin().get<GlobalViewModel>()

    private val reminderAdapter by lazy {
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
        _binding = FragmentReminderChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecView()
        observeLoadingVisible()
        observeEditNote()
        setSwipeToRefreshMove()
        setIntentStart()
    }

    private fun initRecView() {
        with(binding.reminderRecyclerview) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reminderAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .getResultRecyclerByPeriod(startDate(), endDate())
                    .collect {
                        reminderAdapter.setData(it)
                    }
            }
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
                viewModel.noteIdFlow
                    .collect(::editScheduledEvent)
            }
        }
    }

    private fun setSwipeToRefreshMove() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.onSwipeToRefreshMove()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    private fun setIntentStart() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.intentFlow.collect(::startActivity)
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
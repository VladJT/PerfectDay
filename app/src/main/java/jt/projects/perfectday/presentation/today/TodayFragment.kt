package jt.projects.perfectday.presentation.today

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import jt.projects.perfectday.core.extensions.showScheduledEvent
import jt.projects.perfectday.databinding.FragmentTodayBinding
import jt.projects.perfectday.presentation.today.adapter.main.MainListAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodayViewModel by activityViewModel()
    private val todayAdapter by lazy { MainListAdapter(viewModel::onDeleteNoteClicked, viewModel::onEditNoteClicked) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setSwipeToRefreshMove()
        initRecView()
        setLoadingVisible()
        observeEditNote()
    }

    private fun setSwipeToRefreshMove() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.onSwipeToRefreshMove()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    private fun initRecView() {
        binding.todayRecyclerview.adapter = todayAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultRecycler.collect(todayAdapter::submitList)
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
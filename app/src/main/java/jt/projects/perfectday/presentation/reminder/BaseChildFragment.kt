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
import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.core.extensions.showScheduledEvent
import jt.projects.perfectday.databinding.FragmentReminderChildBinding
import kotlinx.coroutines.launch

abstract class BaseChildFragment : Fragment() {
    private var _binding: FragmentReminderChildBinding? = null
    protected val binding get() = _binding!!

    abstract val viewModel: BaseViewModel

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
    }

    abstract fun setSwipeToRefreshMove()

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
                viewModel.noteFlow.collect(::showScheduledEvent)
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
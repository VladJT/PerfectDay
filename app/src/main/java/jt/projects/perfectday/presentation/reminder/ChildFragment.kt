package jt.projects.perfectday.presentation.reminder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import jt.projects.perfectday.core.BaseAdapter
import jt.projects.perfectday.core.GlobalViewModel
import jt.projects.perfectday.core.extensions.editScheduledEvent
import jt.projects.perfectday.databinding.FragmentReminderChildBinding
import jt.projects.perfectday.presentation.congratulation_bottom_dialog.CongratulationBottomDialogFragment
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.java.KoinJavaComponent
import java.time.LocalDate

private const val STATE_CHILD_KEY = "state_child_key"

class ChildFragment : Fragment() {
    private val sharedPref = KoinJavaComponent.getKoin().get<SimpleSettingsPreferences>()
    private lateinit var startDate: LocalDate
    private lateinit var endDate: LocalDate

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
        initArguments()
        initFragmentListener()
        initRecView()
        observeLoadingVisible()
        observeEditNote()
        setSwipeToRefreshMove()
        observeOpenCongratulationDialog()
        setIntentStart()
    }

    private fun initArguments() {
        val stateChild = requireArguments().getInt(STATE_CHILD_KEY)
        val currentDate = LocalDate.now()
        if (stateChild == ReminderFragment.TOMORROW) {
            startDate = currentDate
            endDate = currentDate.plusDays(1)
        } else {
            startDate = currentDate
            endDate = currentDate.plusDays(sharedPref.getDaysPeriodForReminderFragment())
        }
    }

    private fun initFragmentListener() {
        setFragmentResultListener(CongratulationBottomDialogFragment.BUTTON_YES_CLICK) { _, _ ->
            Log.w("TAG", "yesClick")
        }
    }

    private fun initRecView() {
        with(binding.reminderRecyclerview) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reminderAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .getResultRecyclerByPeriod(startDate, endDate)
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
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

    private fun observeOpenCongratulationDialog() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.openCongratulationDialog.collect {
                    CongratulationBottomDialogFragment().show(parentFragmentManager, "Congratulation")
                }
            }
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

    companion object {
        fun newInstance(stateChild: Int): ChildFragment =
            ChildFragment().apply { arguments = bundleOf(STATE_CHILD_KEY to stateChild) }
    }
}
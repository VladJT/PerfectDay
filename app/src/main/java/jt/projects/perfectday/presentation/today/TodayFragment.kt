package jt.projects.perfectday.presentation.today

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.core.extensions.editScheduledEvent
import jt.projects.perfectday.databinding.FragmentTodayBinding
import jt.projects.perfectday.presentation.today.adapter.birth.BirthdayListAdapter
import jt.projects.perfectday.presentation.today.adapter.birth.FriendItem
import jt.projects.perfectday.presentation.today.adapter.note.NoteItem
import jt.projects.perfectday.presentation.today.adapter.note.NoteListAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.time.format.TextStyle
import java.util.Locale

class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodayViewModel by activityViewModel()
    private val birthdayAdapter by lazy { BirthdayListAdapter() }
    private val noteAdapter by lazy {
        NoteListAdapter(
            viewModel::onDeleteNoteClicked,
            viewModel::onEditNoteClicked
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        setSwipeToRefreshMove()
        observeData()
        observeEditNote()
        setMotionProgress()
    }

    private fun initRecyclerView() {
        binding.birthdayHeader.rvBirthday.adapter = birthdayAdapter
        binding.rvTodayNotes.adapter = noteAdapter
    }

    private fun setSwipeToRefreshMove() {
        binding.root.setOnRefreshListener {
            viewModel.onSwipeToRefreshMove()
            binding.root.isRefreshing = false
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.holidayFlow.collect(::setHoliday)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friendsFlow.collect(::setFriends)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.factOfTheDayFlow.collect(::setFactOfTheDay)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notesFlow.collect(::setNotesAndEnabledMotion)
            }
        }
    }

    private fun setHoliday(holiday: DataModel.Holiday) {
        if (holiday == DataModel.Holiday.EMPTY) return
        with(binding.holidayHeader) {
            tvHolidayDate.text = getString(
                R.string.holiday_day,
                holiday.date.dayOfMonth,
                holiday.date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            )
            if (holiday.description.isNullOrEmpty()) {
                tvHolidayDescription.text = holiday.name
            } else {
                tvHolidayDescription.text = getString(
                    R.string.holiday_description,
                    holiday.name,
                    holiday.description
                )
            }
        }
    }

    private fun setFriends(friends: List<FriendItem>) {
        birthdayAdapter.submitList(friends)
    }

    private fun setFactOfTheDay(fact: DataModel.SimpleNotice) {
        binding.factOfTheDayHeader.tvFactDescription.text = fact.description
    }

    private fun setNotesAndEnabledMotion(notes: List<NoteItem>) {
        binding.motionLayout.getTransition(R.id.today_transition).setEnable(notes.isNotEmpty())
        noteAdapter.submitList(notes)
    }

    private fun observeEditNote() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.noteIdFlow.collect(::editScheduledEvent)
            }
        }
    }

    private fun setMotionProgress() {
        binding.motionLayout.progress = viewModel.getProgressMotion()
    }

    override fun onPause() {
        super.onPause()
        viewModel.setProgress(binding.motionLayout.progress)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
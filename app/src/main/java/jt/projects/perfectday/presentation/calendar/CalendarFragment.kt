package jt.projects.perfectday.presentation.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import jt.projects.model.AppState
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.core.extensions.showProgress
import jt.projects.perfectday.databinding.FragmentCalendarBinding
import jt.projects.perfectday.presentation.calendar.dateFragment.ChosenDateDialogFragment
import jt.projects.perfectday.presentation.schedule_event.ScheduleEventDialogFragment
import jt.projects.utils.chosenCalendarDate
import jt.projects.utils.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView
import java.time.LocalDate
import java.util.*


class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()

    private val indicatorsList: MutableList<CalendarView.DateIndicator> = mutableListOf()

    companion object {
        fun newInstance() = CalendarFragment()
    }

    private val viewModel: CalendarViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCalendarViewModel()
    }

    private fun initCalendarViewModel() {
        viewModel.liveDataForViewToObserve.observe(this@CalendarFragment) {
            renderData(it)
        }
        viewModel.loadData()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                showLoadingFrame(false)
                appState.data?.let { data ->
                    initCalendarView(data)
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

    private fun initCalendarView(data: List<DataModel>) {
        calendar.set(CalendarDate.today.year, CalendarDate.today.month, 1)
        with(binding.calendarView) {
            setupCalendar(
                initialDate = CalendarDate(calendar.time),
                firstDayOfWeek = Calendar.MONDAY,
                showYearSelectionView = true
            )
            indicatorsList.clear()
            initBirthdayList(data)

            datesIndicators = indicatorsList

            onDateClickListener = { date ->
                showChosenDateDialogFragmentDialog(date)
            }

            onDateLongClickListener = { date ->
                chosenCalendarDate = LocalDate.of(date.year, date.month + 1, date.dayOfMonth)
                showScheduledEvent()
            }
        }
    }

    fun showScheduledEvent() {
        val scheduleEventDialogFragment = ScheduleEventDialogFragment()
        scheduleEventDialogFragment.show(
            requireActivity().supportFragmentManager,
            "ScheduleEventDialogFragment"
        )
    }

    private fun showChosenDateDialogFragmentDialog(date: CalendarDate) {
        val chosenDateDialogFragment = ChosenDateDialogFragment(date)
        chosenDateDialogFragment.show(
            requireActivity().supportFragmentManager,
            "CHOSEN_DATE_DIALOG_FRAGMENT"
        )
    }

    private fun showLoadingFrame(isLoading: Boolean) {
        binding.loadingFrameLayout.isVisible = isLoading
    }

    private fun initBirthdayList(data: List<DataModel>) {
        val calendarSetter = Calendar.getInstance()
        for (index in data.indices) {
            when (data[index]) {

                is DataModel.BirthdayFromPhone -> {
                    val birthdayData = data[index] as DataModel.BirthdayFromPhone
                    if (birthdayData.birthDate.monthValue < LocalDate.now().monthValue ||
                        (birthdayData.birthDate.monthValue == LocalDate.now().monthValue && (birthdayData.birthDate.dayOfMonth < LocalDate.now().dayOfMonth))
                    ) {
                        calendarSetter.set(
                            CalendarDate.today.year + 1,
                            birthdayData.birthDate.monthValue - 1,
                            birthdayData.birthDate.dayOfMonth
                        )
                    } else {
                        calendarSetter.set(
                            CalendarDate.today.year,
                            birthdayData.birthDate.monthValue - 1,
                            birthdayData.birthDate.dayOfMonth
                        )
                    }

                    indicatorsList.add(
                        DateIndicator(
                            resources.getColor(R.color.md_theme_light_primary),
                            CalendarDate(calendarSetter.time)
                        )
                    )
                }

                is DataModel.BirthdayFromVk -> {
                    val birthdayData = data[index] as DataModel.BirthdayFromVk
                    if (birthdayData.birthDate.monthValue < LocalDate.now().monthValue ||
                        (birthdayData.birthDate.monthValue == LocalDate.now().monthValue && (birthdayData.birthDate.dayOfMonth < LocalDate.now().dayOfMonth))
                    ) {
                        calendarSetter.set(
                            CalendarDate.today.year + 1,
                            birthdayData.birthDate.monthValue - 1,
                            birthdayData.birthDate.dayOfMonth
                        )
                    } else {
                        calendarSetter.set(
                            CalendarDate.today.year,
                            birthdayData.birthDate.monthValue - 1,
                            birthdayData.birthDate.dayOfMonth
                        )
                    }

                    indicatorsList.add(
                        DateIndicator(
                            resources.getColor(R.color.md_theme_light_primary),
                            CalendarDate(calendarSetter.time)
                        )
                    )
                }

                is DataModel.ScheduledEvent -> {
                    val eventData = data[index] as DataModel.ScheduledEvent
                    if (eventData.date.monthValue < LocalDate.now().monthValue ||
                        ((eventData.date.monthValue == LocalDate.now().monthValue && (eventData.date.dayOfMonth < LocalDate.now().dayOfMonth)))
                    ) {
                        calendarSetter.set(
                            CalendarDate.today.year + 1,
                            eventData.date.monthValue - 1,
                            eventData.date.dayOfMonth
                        )
                    } else {
                        calendarSetter.set(
                            CalendarDate.today.year,
                            eventData.date.monthValue - 1,
                            eventData.date.dayOfMonth
                        )
                    }

                    indicatorsList.add(
                        DateIndicator(
                            resources.getColor(R.color.red),
                            CalendarDate(calendarSetter.time)
                        )
                    )
                }
                else -> {}
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
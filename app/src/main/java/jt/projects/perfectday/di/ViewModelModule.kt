package jt.projects.perfectday.di

import android.content.Context
import jt.projects.perfectday.App
import jt.projects.perfectday.presentation.calendar.CalendarViewModel
import jt.projects.perfectday.presentation.calendar.dateFragment.ChosenDateViewModel
import jt.projects.perfectday.presentation.reminder.TomorrowViewModel
import jt.projects.perfectday.presentation.reminder.PeriodViewModel
import jt.projects.perfectday.presentation.schedule_event.ScheduleEventViewModel
import jt.projects.perfectday.presentation.settings.PushSettingViewModel
import jt.projects.perfectday.presentation.settings.SettingsViewModel
import jt.projects.perfectday.presentation.today.TodayViewModel
import jt.projects.utils.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val viewModelModule = module {

    viewModel {
        TodayViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            simpleNoticeInteractorImpl = get(),
            holidayInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractorImpl = get()
        )
    }

    viewModel {
        TomorrowViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractor = get(),
            phoneBookProvider = get(),
            dateStrategy = get(named(DATE_STATEGY_TOMORROW))
        )
    }

    viewModel {
        PeriodViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractor = get(),
            phoneBookProvider = get(),
            dateStrategy = get(named(DATE_STATEGY_PERIOD))
        )
    }

    viewModel {
        CalendarViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractor = get(),
            phoneBookProvider = get(),
            dateStrategy = get(named(DATE_STATEGY_ALLDATES))
        )
    }

    viewModel {
        ChosenDateViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractor = get(),
            phoneBookProvider = get(),
            dateStrategy = get(named(DATE_STATEGY_CHOSEN_CALENDER_DATE))
        )
    }

    viewModel {
        ScheduleEventViewModel(scheduledEventInteractor = get())
    }

    viewModel {
        SettingsViewModel(
            settingsPref = get(), vkRepository = get(), scheduledEventInteractor = get()
        )
    }

    viewModel{
        PushSettingViewModel(sharedPref = get())
    }

}

package jt.projects.perfectday.di

import jt.projects.perfectday.core.BaseViewModel
import jt.projects.perfectday.presentation.schedule_event.ScheduleEventViewModel
import jt.projects.perfectday.presentation.settings.PushSettingViewModel
import jt.projects.perfectday.presentation.settings.SettingsViewModel
import jt.projects.perfectday.presentation.today.TodayViewModel
import jt.projects.utils.DATE_STATEGY_ALLDATES
import jt.projects.utils.DATE_STATEGY_CHOSEN_CALENDER_DATE
import jt.projects.utils.DATE_STATEGY_PERIOD
import jt.projects.utils.DATE_STATEGY_TODAY
import jt.projects.utils.DATE_STATEGY_TOMORROW
import jt.projects.utils.VM_CALENDAR
import jt.projects.utils.VM_CHOSEN_DATE
import jt.projects.utils.VM_DAILY_REMINDER
import jt.projects.utils.VM_REMINDER_PERIOD
import jt.projects.utils.VM_REMINDER_TOMORROW
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

    viewModel(named(VM_REMINDER_TOMORROW)) {
        BaseViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractor = get(),
            phoneBookProvider = get(),
            dateStrategy = get(named(DATE_STATEGY_TOMORROW))
        )
    }

    viewModel(named(VM_REMINDER_PERIOD)) {
        BaseViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractor = get(),
            phoneBookProvider = get(),
            dateStrategy = get(named(DATE_STATEGY_PERIOD))
        )
    }

    viewModel(named(VM_DAILY_REMINDER)) {
        BaseViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractor = get(),
            phoneBookProvider = get(),
            dateStrategy = get(named(DATE_STATEGY_TODAY))
        )
    }

    viewModel(named(VM_CALENDAR)) {
        BaseViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractor = get(),
            phoneBookProvider = get(),
            dateStrategy = get(named(DATE_STATEGY_ALLDATES))
        )
    }

    viewModel(named(VM_CHOSEN_DATE)) {
        BaseViewModel(
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


    viewModel {
        PushSettingViewModel(sharedPref = get())
    }

}

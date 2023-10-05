package jt.projects.perfectday.di

import jt.projects.perfectday.core.GlobalViewModel
import jt.projects.perfectday.presentation.schedule_event.ScheduleEventViewModel
import jt.projects.perfectday.presentation.settings.PushSettingViewModel
import jt.projects.perfectday.presentation.settings.SettingsViewModel
import jt.projects.perfectday.presentation.today.TodayViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    single {
        TodayViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            simpleNoticeInteractorImpl = get(),
            holidayInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractorImpl = get()
        )
    }

    single {
        GlobalViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractor = get(),
            phoneBookProvider = get()
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

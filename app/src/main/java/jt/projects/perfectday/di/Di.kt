package jt.projects.perfectday.di

import android.content.Context
import androidx.room.Room
import jt.projects.perfectday.App
import jt.projects.perfectday.core.PhoneBookProvider
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.HolidayInteractorImpl
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.perfectday.presentation.calendar.CalendarViewModel
import jt.projects.perfectday.presentation.calendar.dateFragment.ChosenDateViewModel
import jt.projects.perfectday.presentation.reminder.LeftChildViewModel
import jt.projects.perfectday.presentation.reminder.RightChildViewModel
import jt.projects.perfectday.presentation.schedule_event.ScheduleEventViewModel
import jt.projects.perfectday.presentation.settings.SettingsViewModel
import jt.projects.perfectday.presentation.today.TodayViewModel
import jt.projects.repository.network.retrofit.DataSourceHoliday
import jt.projects.repository.network.retrofit.holiday.HolidayRepository
import jt.projects.repository.network.retrofit.holiday.HolidayRepositoryImpl
import jt.projects.repository.network.retrofit.holiday.RetrofitHolidayImpl
import jt.projects.repository.network.retrofit.holiday.dto.HolidayDTO
import jt.projects.repository.room.LocalRepository
import jt.projects.repository.room.RoomDatabaseImpl
import jt.projects.repository.room.ScheduledEventDatabase
import jt.projects.utils.network.OnlineStatusLiveData
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.SimpleSharedPref
import jt.projects.utils.ui.CoilImageLoader
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


// зависимости, используемые во всём приложении
val application = module {

    // контекст приложения
    single<App> { androidApplication().applicationContext as App }

    // статус сети
    single { OnlineStatusLiveData(context = get()) }

    // загрузчик изображений
    single { CoilImageLoader() }

    // локальные настройки
    single<SimpleSettingsPreferences> {
        SimpleSharedPref(
            get<App>().getSharedPreferences(
                SimpleSharedPref.SP_DB_NAME, Context.MODE_PRIVATE
            )
        )
    }

    // работа с телефонной книгой
    single<PhoneBookProvider> { PhoneBookProvider(context = get()) }
}


val roomModule = module {
    single {
        Room.databaseBuilder(get(), ScheduledEventDatabase::class.java, "scheduledEvents.db")
            .fallbackToDestructiveMigration().build()
    }

    single { get<ScheduledEventDatabase>().dao() }
}


val repoModule = module {
    single<LocalRepository> { RoomDatabaseImpl(dao = get()) }
}


val interactorsModule = module {
    single { BirthdayFromPhoneInteractorImpl(androidApplication().applicationContext) }
    single { SimpleNoticeInteractorImpl(repository = get()) }
    single { GetFriendsFromVkUseCase(vkNetworkRepository = get()) }
    single { ScheduledEventInteractorImpl(repository = get()) }

    //HolidayDi
    single { HolidayInteractorImpl(repository = get()) }
    single<HolidayRepository> { HolidayRepositoryImpl(dataSource = get()) }
    single<DataSourceHoliday<List<HolidayDTO>>> { RetrofitHolidayImpl() }
}


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
        LeftChildViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractor = get(),
            phoneBookProvider = get()
        )
    }

    viewModel {
        RightChildViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractor = get(),
            phoneBookProvider = get()
        )
    }

    viewModel {
        CalendarViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractor = get(),
        )
    }

    viewModel {
        ChosenDateViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractor = get(),
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
}

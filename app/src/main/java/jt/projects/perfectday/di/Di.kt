package jt.projects.perfectday.di

import android.content.Context
import androidx.room.Room
import jt.projects.perfectday.App
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.GetFriendsFromVkUseCase
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.perfectday.presentation.dialogs.ScheduleEventViewModel
import jt.projects.perfectday.presentation.settings.SettingsViewModel
import jt.projects.perfectday.presentation.today.TodayViewModel
import jt.projects.repository.retrofit.facts.FactsRepoImpl
import jt.projects.repository.retrofit.facts.FactsRepository
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
                SimpleSharedPref.SP_DB_NAME,
                Context.MODE_PRIVATE
            )
        )
    }
}

val roomModule = module {
    single {
        Room.databaseBuilder(get(), ScheduledEventDatabase::class.java, "scheduledEvents.db")
            .build()
    }

    single { get<ScheduledEventDatabase>().dao() }
}


val repoModule = module {
    single<FactsRepository> { FactsRepoImpl() }
    single<LocalRepository> { RoomDatabaseImpl(dao = get()) }
}


val interactorsModule = module {
    single { BirthdayFromPhoneInteractorImpl() }
    single { SimpleNoticeInteractorImpl(repository = get()) }
    single { GetFriendsFromVkUseCase(vkNetworkRepository = get()) }
    single { ScheduledEventInteractorImpl(repository = get()) }
}


val viewModelModule = module {
    viewModel {
        SettingsViewModel(settingsPref = get(), vkRepository = get())
    }

    viewModel {
        ScheduleEventViewModel(scheduledEventInteractorImpl = get())
    }

    viewModel {
        TodayViewModel(
            settingsPreferences = get(),
            birthdayFromPhoneInteractor = get(),
            simpleNoticeInteractorImpl = get(),
            getFriendsFromVkUseCase = get(),
            scheduledEventInteractorImpl = get()
        )
    }
}

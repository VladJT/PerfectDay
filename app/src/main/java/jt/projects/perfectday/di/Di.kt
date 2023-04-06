package jt.projects.perfectday.di

import android.content.Context
import jt.projects.perfectday.App
import jt.projects.perfectday.interactors.*
import jt.projects.perfectday.presentation.settings.SettingsViewModel
import jt.projects.perfectday.presentation.today.TodayViewModel
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
    single { OnlineStatusLiveData(get()) }

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

val interactors = module {
    single { BirthdayFromPhoneInteractorImpl() }
    single { SimpleNoticeInteractorImpl() }
}

val viewModelModule = module {
    viewModel {
        SettingsViewModel(settingsPref = get())
    }

    viewModel {
        TodayViewModel(
            birthdayFromPhoneInteractor = get(),
            simpleNoticeInteractorImpl = get()
        )
    }
}
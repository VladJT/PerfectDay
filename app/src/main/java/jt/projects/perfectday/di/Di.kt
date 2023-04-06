package jt.projects.perfectday.di

import android.content.Context
import jt.projects.perfectday.App
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.SimpleNoticeInteractorImpl
import jt.projects.perfectday.presentation.today.TodayViewModel
import jt.projects.utils.NETWORK_SERVICE
import jt.projects.utils.network.OnlineStatusLiveData
import jt.projects.utils.shared_preferences.SimpleSharedPref
import jt.projects.utils.ui.CoilImageLoader
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
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
    single {
        SimpleSharedPref(
            get<App>().getSharedPreferences(
                SimpleSharedPref.SP_DB_NAME,
                Context.MODE_PRIVATE
            )
        )
    }
}

//зависимости конкретного экрана
val todayFragment = module {
    viewModel { TodayViewModel(get(), get()) }
}

val interactors = module {
    single { BirthdayFromPhoneInteractorImpl() }
    single { SimpleNoticeInteractorImpl() }
}
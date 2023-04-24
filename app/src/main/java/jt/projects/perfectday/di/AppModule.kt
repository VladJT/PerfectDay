package jt.projects.perfectday.di

import android.content.Context
import jt.projects.perfectday.App
import jt.projects.perfectday.core.PhoneBookProvider
import jt.projects.perfectday.push.NotificationProvider
import jt.projects.utils.network.OnlineStatusLiveData
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import jt.projects.utils.shared_preferences.SimpleSharedPref
import jt.projects.utils.ui.CoilImageLoader
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

// зависимости, используемые во всём приложении
val appModule = module {

    // контекст приложения
    single<App> { androidApplication().applicationContext as App }

    // статус сети
    single { OnlineStatusLiveData(context = get()) }

    // SEND PUSH
    single { NotificationProvider(context = get()) }

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
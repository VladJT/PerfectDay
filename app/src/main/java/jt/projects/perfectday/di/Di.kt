package jt.projects.perfectday.di

import android.content.Context
import jt.projects.perfectday.App
import jt.projects.utils.ui.CoilImageLoader
import jt.projects.utils.NETWORK_SERVICE
import jt.projects.utils.network.INetworkStatus
import jt.projects.utils.network.NetworkStatus
import jt.projects.utils.shared_preferences.SimpleSharedPref
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module


// зависимости, используемые во всём приложении
val application = module {

    single<App> { androidApplication().applicationContext as App }

    single<INetworkStatus>(qualifier = named(NETWORK_SERVICE)) { NetworkStatus(get()) }

    single { CoilImageLoader() }

    single {
        SimpleSharedPref(
            get<App>().getSharedPreferences(
                SimpleSharedPref.SP_DB_NAME,
                Context.MODE_PRIVATE
            )
        )
    }
}
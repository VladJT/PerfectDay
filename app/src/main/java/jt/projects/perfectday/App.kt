package jt.projects.perfectday

import android.app.Application
import jt.projects.perfectday.di.*
import jt.projects.repository.network.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(application, viewModelModule, networkModule, interactors))
        }
    }
}

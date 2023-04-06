package jt.projects.perfectday

import android.app.Application
import jt.projects.perfectday.di.application
import jt.projects.perfectday.di.interactors
import jt.projects.perfectday.di.todayFragment
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(application, todayFragment, interactors))
        }
    }
}

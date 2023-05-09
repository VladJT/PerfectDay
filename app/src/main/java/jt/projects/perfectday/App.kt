package jt.projects.perfectday

import android.app.Application
import android.content.Context
import android.content.res.Resources
import jt.projects.perfectday.di.appModule
import jt.projects.perfectday.di.interactorsModule
import jt.projects.perfectday.di.repoModule
import jt.projects.perfectday.di.roomModule
import jt.projects.perfectday.di.viewModelModule
import jt.projects.repository.network.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    companion object {
        private var mContext: Context? = null

        fun getResources(): Resources {
            return mContext!!.resources
        }

        fun getContext(): Context {
            return mContext!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    appModule,
                    viewModelModule,
                    interactorsModule,
                    repoModule,
                    networkModule,
                    roomModule
                )
            )
        }
    }
}

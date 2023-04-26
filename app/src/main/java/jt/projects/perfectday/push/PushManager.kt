package jt.projects.perfectday.push

import android.app.Application
import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class PushManager(private val repoManager: PushManagerRepo) : KoinComponent {

    private val context: Context = get()

    fun startWork() {
        repoManager.startPushManager(context)
    }

    fun stopWork(){
        repoManager.stopPushManager(context)
    }

}
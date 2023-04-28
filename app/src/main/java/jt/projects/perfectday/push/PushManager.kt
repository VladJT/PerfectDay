package jt.projects.perfectday.push

import android.content.Context

class PushManager(
    private val context: Context,
    private val repoManager: PushManagerRepo
) {

    fun startWork() {
        repoManager.startPushManager(context)
    }

    fun stopWork() {
        repoManager.stopPushManager(context)
    }

    fun showTimeDelay(){
        repoManager.timeStartPushManager()
    }


}
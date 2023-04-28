package jt.projects.perfectday.push

import android.content.Context

interface PushManagerRepo {

    fun startPushManager(context: Context)
    fun stopPushManager(context: Context)

    fun timeStartPushManager()

}
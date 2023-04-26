package jt.projects.perfectday.push

import android.content.Context
import android.util.Log
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters

class PushWorker(context: Context, param: WorkerParameters) : Worker(context, param) {

    override fun doWork(): Result {

        try {
            Log.d(PushService.TAG, "doWork: start")
            val pushService = PushService()
            pushService.pushBirthdayToday()

        } catch (e: IllegalAccessException) {
            return  Result.failure()
        }

        Log.d(PushService.TAG, "doWork: end")
        return Result.success()
    }
}
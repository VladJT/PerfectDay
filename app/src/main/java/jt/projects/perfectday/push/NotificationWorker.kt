package jt.projects.perfectday.push

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(private val appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {

    companion object {
        const val TAG = "DailyReminder"
    }

    override fun doWork(): Result {
        NotificationProvider(appContext).send("2", "4")
        return Result.success()
    }
}

package jt.projects.perfectday.push

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.java.KoinJavaComponent.getKoin

class NotificationWorker(private val appContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(appContext, workerParameters) {

    companion object {
        const val TAG = "DailyReminder"
    }

    private val notificationProvider by lazy { getKoin().get<NotificationProvider>() }

    override suspend fun doWork(): Result {
        return try {
            notificationProvider.sendTodayInfo()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

}

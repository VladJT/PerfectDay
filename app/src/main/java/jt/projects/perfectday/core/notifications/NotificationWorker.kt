package jt.projects.perfectday.core.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import org.koin.java.KoinJavaComponent.getKoin
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationWorker(private val appContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(appContext, workerParameters) {

    companion object {
        private const val WORK_TAG = "DailyReminder"

        fun scheduleEverydayNotificationJob(appContext: Context, hour: Int, minute: Int) {
            val calendar = Calendar.getInstance()
            val nowMillis = calendar.timeInMillis

            if (calendar[Calendar.HOUR_OF_DAY] > hour || calendar[Calendar.HOUR_OF_DAY] == hour && calendar[Calendar.MINUTE] + 1 >= minute) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            calendar[Calendar.HOUR_OF_DAY] = hour
            calendar[Calendar.MINUTE] = minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0

            val diff = (calendar.timeInMillis - nowMillis) / 1000

            cancelEverydayNotificationJob(appContext)

            val notificationWork =
                PeriodicWorkRequest.Builder(NotificationWorker::class.java, 1, TimeUnit.DAYS)
                    .setInitialDelay(diff, TimeUnit.SECONDS)
                    .addTag(WORK_TAG)
                    .build()

            WorkManager.getInstance(appContext)
                .enqueueUniquePeriodicWork(
                    WORK_TAG,
                    ExistingPeriodicWorkPolicy.UPDATE, notificationWork
                )
        }

        fun cancelEverydayNotificationJob(appContext: Context) {
            WorkManager.getInstance(appContext).cancelAllWorkByTag(WORK_TAG)
        }
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

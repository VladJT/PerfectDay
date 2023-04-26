package jt.projects.perfectday.push

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.*
import jt.projects.utils.PUSH_NOTIFICATION_STARTHOUR
import jt.projects.utils.PUSH_NOTIFICATION_STARTMINUTE
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import java.time.Duration
import java.util.*

class PushManagerImpl(private val settingsPreferences: SimpleSettingsPreferences) :
    PushManagerRepo {

    @SuppressLint("RestrictedApi")
    override fun startPushManager(context: Context) {

        val constraints = Constraints.Builder().setRequiredNetworkType(
            NetworkType.CONNECTED
        ).build()
        val data = Data.Builder().put(PushService.ID_DATA, "1111111")
        val syncWorkReqest: PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(PushWorker::class.java, Duration.ofMinutes(15))
                .setId(PushService.uuidWork).addTag(PushService.WORK_ID).setInputData(data.build())
                .setInitialDelay(2, java.util.concurrent.TimeUnit.SECONDS)
                .setConstraints(constraints).build()

        WorkManager.getInstance(context).enqueue(syncWorkReqest)

    }

    override fun stopPushManager(context: Context) {
        WorkManager.getInstance(context).getWorkInfosByTag(PushService.WORK_ID).cancel(true)
    }

    private fun getDelayTime() {
        val startHours =
            settingsPreferences.getSettings(PUSH_NOTIFICATION_STARTHOUR)?.toIntOrNull() ?: 0
        val startMinute =
            settingsPreferences.getSettings(PUSH_NOTIFICATION_STARTMINUTE)?.toIntOrNull() ?: 0

        if (startHours == 0 && startMinute == 0) {
            return
        }

        val c = Calendar.getInstance()

    }

}
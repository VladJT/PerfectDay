package jt.projects.perfectday.push

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.*
import jt.projects.utils.DEBUG
import jt.projects.utils.LOG_TAG
import jt.projects.utils.PUSH_NOTIFICATION_STARTHOUR
import jt.projects.utils.PUSH_NOTIFICATION_STARTMINUTE
import jt.projects.utils.shared_preferences.SimpleSettingsPreferences
import okhttp3.internal.UTC
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.ZonedDateTime
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
            PeriodicWorkRequest.Builder(PushWorker::class.java, Duration.ofSeconds(900))
                .setId(PushService.uuidWork).addTag(PushService.WORK_ID).setInputData(data.build())
                .setInitialDelay(getDelayTime(), java.util.concurrent.TimeUnit.MILLISECONDS)
                .setConstraints(constraints).build()

        WorkManager.getInstance(context).enqueue(syncWorkReqest)

    }

    override fun stopPushManager(context: Context) {
        WorkManager.getInstance(context).getWorkInfosByTag(PushService.WORK_ID).cancel(true)
    }

    override fun timeStartPushManager() {
        getDelayTime()
    }

    private fun getDelayTime(): Long {
        var startHours =
            settingsPreferences.getSettings(PUSH_NOTIFICATION_STARTHOUR) ?: "0"
        var startMinute =
            settingsPreferences.getSettings(PUSH_NOTIFICATION_STARTMINUTE) ?: "0"

        if (startHours == "0" && startMinute == "0") {
            return 0
        }
        if (startHours == "0" || startHours.length == 1) {
            "0".plus(startHours)
        }

        if (startMinute == "0" || startMinute.length == 1) {
            "0".plus(startMinute)
        }


        val systemDt = ZonedDateTime.now(UTC.toZoneId())
        val formatDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

        val timeUp: Date? =
            formatDate.parse("${systemDt.year}/${systemDt.month.value}/${systemDt.dayOfMonth} ${startHours}:${startMinute}:00")

        val diff: Long = (timeUp?.time ?: 0) - System.currentTimeMillis()
        val diffSecond = diff / 1000 % 60
        val diffMinutes = diff / (60 * 1000) % 60
        val diffHours = diff / (60 * 60 * 1000) % 24

        val diffDays = diff / (24 * 60 * 60 * 1000)

        if (DEBUG) {
            Log.d(LOG_TAG, "current Time: час ${systemDt.hour} минута ${systemDt.minute}")
            Log.d(LOG_TAG, "start Time: час $startHours минута $startMinute")
            Log.d(
                LOG_TAG,
                "getDelayTime: дней $diffDays часов $diffHours минут $diffMinutes секунд $diffSecond"
            )
            Log.d(LOG_TAG, "отклонение ${diff.toString()}")
        }
        val systemDataTime: Date =
            formatDate.parse("${systemDt.year}/${systemDt.month.value}/${systemDt.dayOfMonth} ${systemDt.hour}:${systemDt.minute}:00") as Date
        var delayStart: Long = 0
        if (diff < 0) {
            //time AM т.е.
            val systemNextData = systemDt.plusDays(1)

            val nextDataTimeFromCurrentSystem: Date =
                formatDate.parse("${systemNextData.year}/${systemNextData.month.value}/${systemNextData.dayOfMonth} ${systemNextData.hour}:${systemNextData.minute}:00") as Date

            delayStart = nextDataTimeFromCurrentSystem.time - systemDataTime.time + diff

        } else {
            delayStart = diff
        }

        return delayStart
    // delayStart/1000
    }

}
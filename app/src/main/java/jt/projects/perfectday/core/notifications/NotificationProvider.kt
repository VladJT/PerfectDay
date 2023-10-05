package jt.projects.perfectday.core.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.core.GlobalViewModel
import jt.projects.perfectday.presentation.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import java.time.LocalDate

class NotificationProvider(
    private val appContext: Context
) {
    // NOTIFICATIONS
    companion object {
        const val channelGroupId = "group_1"
        const val channelGroupName = "Каналы для PerfectDay"

        const val channelId = "channel_pd_high"
        const val channelName = "PerfectDay_Channel"
        const val channelDescription = "Канал c IMPORTANCE_HIGH"

        const val messagePriority = NotificationCompat.PRIORITY_MAX
        const val messageId = 444
    }

    init {
        initNotificationChannels()
    }

    private var job: Job? = null

    val viewModel = getKoin().get<GlobalViewModel>()

    fun sendTodayInfo() {

        val startDate = LocalDate.now()
        val endDate = LocalDate.now()

        job?.cancel()
        CoroutineScope(Dispatchers.Default).launch {
            viewModel
                .getResultRecyclerByPeriod(startDate, endDate)
                .collect {
                val notesCount = it.filterIsInstance<DataModel.ScheduledEvent>().size
                val birthdaysCount = it.filterIsInstance<DataModel.BirthdayFromPhone>().size +
                        it.filterIsInstance<DataModel.BirthdayFromVk>().size

                send(
                    "Сегодня",
                    "Дней рождения: $birthdaysCount, дел: ${notesCount}"
                )
            }
        }
    }

    fun checkPermissionPostNotifications(): Boolean =
        (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED)

    fun checkPermissionAutoStartApplication(): Boolean =
        (ActivityCompat.checkSelfPermission(
            appContext,
            Manifest.permission.RECEIVE_BOOT_COMPLETED
        )
                == PackageManager.PERMISSION_GRANTED)

    fun openAutoStartSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_SETTINGS
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("android.provider.extra.APP_PACKAGE", appContext.packageName)
        appContext.startActivity(intent)
    }

    fun openNotificationsSettings() {
        val intent = Intent()
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("android.provider.extra.APP_PACKAGE", appContext.packageName)
        appContext.startActivity(intent)
    }

    @SuppressLint("ResourceAsColor")
    fun send(title: String, message: String): Boolean {
        val builder = NotificationCompat.Builder(appContext, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentTitle(title)
            .setContentText(message)
            .setLights(R.color.primary_text_color, 3000, 1000)
            .setPriority(messagePriority)
            .addAction(R.drawable.ic_home, appContext.getString(R.string.open), getActivityIntent())
            .setAutoCancel(true)


        return if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(appContext).notify(messageId, builder.build())
            true
        } else {
            false
        }
    }

    // инициализация каналов нотификаций
    private fun initNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.run {
                createNotificationChannelGroup(
                    NotificationChannelGroup(
                        channelGroupId,
                        channelGroupName
                    )
                )
                createNotificationChannel(
                    NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                        .apply {
                            description = channelDescription
                            group = channelGroupId
                        })
            }
        }
    }

    private fun getActivityIntent(): PendingIntent {
        val intent = Intent(appContext, MainActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(appContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }
}
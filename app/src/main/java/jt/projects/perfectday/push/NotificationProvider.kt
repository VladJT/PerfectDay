package jt.projects.perfectday.push

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import jt.projects.perfectday.R

class NotificationProvider(private val context: Context) {
    // NOTIFICATIONS
    companion object {
        const val channelGroupId = "group_1"
        const val channelGroupName = "Каналы для PerfectDay"

        const val channelId = "channel_pd_high"
        const val channelName = "PerfectDay_Channel"
        const val channelDescription = "Канал c IMPORTANCE_HIGH"

        const val messagePriority = NotificationCompat.PRIORITY_DEFAULT
        const val messageId = 444
    }

    init {
        initNotificationChannels()
    }


    fun checkPermissionPostNotifications(): Boolean =
        (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED)

    fun checkPermissionAutoStartApplication(): Boolean =
        (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                == PackageManager.PERMISSION_GRANTED)

    fun openAutoStartSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_SETTINGS
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
        context.startActivity(intent)
    }

    fun openNotificationsSettings() {
        val intent = Intent()
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
        context.startActivity(intent)
    }

    @SuppressLint("ResourceAsColor")
    fun send(title: String, message: String): Boolean {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(message)
            .setLights(R.color.primary_text_color, 3000, 1000)
            .setPriority(messagePriority)

        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(messageId, builder.build())
            true
        } else {
            false
        }
    }

    // инициализация каналов нотификаций
    private fun initNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.run {
                createNotificationChannelGroup(
                    NotificationChannelGroup(
                        channelGroupId,
                        channelGroupName
                    )
                )
                createNotificationChannel(
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                        .apply {
                            description = channelDescription
                            group = channelGroupId
                        })
            }
        }
    }
}
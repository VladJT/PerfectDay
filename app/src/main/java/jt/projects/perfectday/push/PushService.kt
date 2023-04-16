package jt.projects.perfectday.push

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import jt.projects.perfectday.R
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.presentation.MainActivity
import jt.projects.perfectday.presentation.schedule_event.ScheduleEventDialogFragment.Companion.TAG
import jt.projects.repository.push.DataPush
import jt.projects.repository.push.PushBirthdayRepo
import jt.projects.repository.push.PushBirthdayRepoImpl
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.time.LocalDate

class PushService : KoinComponent {

    private val repoPushBirthday: PushBirthdayRepo = PushBirthdayRepoImpl()
    private val context: Context = get<Context>().applicationContext

    //
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    companion object {
        const val CHANNEL_BIRTHDAY = "channel_birthday"
        const val CHANNEL_EVENT = "channel_event"

    }

    val intent = Intent(context?.applicationContext, MainActivity::class.java)
    val contextIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    val job = CoroutineScope(
        Dispatchers.IO +
                SupervisorJob() +
                CoroutineExceptionHandler { _, throwable ->
                    handleError(throwable)

                }
    )

    private fun handleError(throwable: Throwable) {

        if (jt.projects.utils.DEBUG) {
            Log.d("push_@", "handleError: ${throwable.message}")
        }
    }


    fun pushBirthdayToday() {
        val dataPush = repoPushBirthday.getDataTest()
        val listPush = mutableListOf<DataPush>()

        job.launch {
            val birthday = async {  BirthdayFromPhoneInteractorImpl(context).getContactsByDay(localDate = LocalDate.now())}
            birthday.await().map { DataPush(it.name, it.age.toString()) }.forEach { listPush.add(it) }
        }

        if (listPush.isNotEmpty()) {

            var notification_id = 1
            dataPush.forEach {
                val notificationBuilderLow =
                    NotificationCompat.Builder(context, CHANNEL_BIRTHDAY).apply {
                        setSmallIcon(R.drawable.ic_push)
                        setContentTitle(it.title)
                        setContentText(it.message)
                        setContentIntent(contextIntent)
                        setAutoCancel(true)
                        priority = NotificationManager.IMPORTANCE_LOW
                    }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channelNameLow = "Name $CHANNEL_BIRTHDAY"
                    val channelDescriptionLow = "Description: $CHANNEL_BIRTHDAY"
                    val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
                    val channelLow =
                        NotificationChannel(
                            CHANNEL_BIRTHDAY,
                            channelNameLow,
                            channelPriorityLow
                        ).apply {
                            description = channelDescriptionLow
                        }
                    notificationManager.createNotificationChannel(channelLow)
                }

                notificationManager.notify(notification_id++, notificationBuilderLow.build())
            }


        }
    }


}
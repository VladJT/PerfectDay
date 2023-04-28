package jt.projects.perfectday.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import jt.projects.perfectday.R
import jt.projects.perfectday.interactors.BirthdayFromPhoneInteractorImpl
import jt.projects.perfectday.interactors.ScheduledEventInteractorImpl
import jt.projects.perfectday.presentation.MainActivity
import jt.projects.repository.push.DataPush
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.time.LocalDate
import java.util.*

class PushService : KoinComponent {

    private val context: Context = get<Context>().applicationContext
    private val scheduledEventIterctor: ScheduledEventInteractorImpl = get()

    //
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val intent = Intent(context?.applicationContext, MainActivity::class.java)
    val contextIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )


    companion object {

        const val CHANNEL_BIRTHDAY = "channel_birthday"
        const val CHANNEL_EVENT = "channel_event"
        const val CHANNEL_PERFECT = "channel perfect day"
        const val TAG = "push_@"

        const val WORK_ID = "psh_work_manager"
        const val ID_DATA = "time_start"
        const val TAG_PARAM = "TAG_WORK_PUSH"

        val uuidWork = UUID.randomUUID()
       // val uuidWork = UUID.fromString("b23988e7-719b-4858-a7c0-f5bf41848779")

        fun newInstance(): PushService {
            return PushService()

        }
    }

    val job = CoroutineScope(
        Dispatchers.Main +
                SupervisorJob() +
                CoroutineExceptionHandler { _, throwable ->
                    handleError(throwable)

                }
    )

    private var listPushBday = mutableListOf<DataPush>()
    private val listPushNotice = mutableListOf<DataPush>()
    private var listPush = mutableListOf<DataPush>()
    private fun handleError(throwable: Throwable) {

        if (jt.projects.utils.DEBUG) {
            Log.d(TAG, "handleError: ${throwable.message}")
        }
    }


    fun pushBirthdayToday() {

        job.launch {
            getBD(object : OnchangeDataBD {
                override fun onChange(lisResult: List<DataPush>) {
                    listPush.addAll(lisResult)
//                    sendPush(listPush, CHANNEL_BIRTHDAY)
                }
            })

            getEvent(object : OnchangeDataBD {

                override fun onChange(lisResult: List<DataPush>) {
                    listPush.addAll(lisResult)
//                    sendPush(listPush, CHANNEL_EVENT)
                }
            }
            )
    //        sendPush(listPush, CHANNEL_PERFECT)
            sendPushOne(listPush, CHANNEL_PERFECT)
        }

    }

    fun sendPushOne(listPush: List<DataPush>, channel: String) {
        val quantityBd = listPush.count { it.typePush == CHANNEL_BIRTHDAY }
        val event = listPush.count { it.typePush == CHANNEL_EVENT }
        val result = "Дни рождения: ${if(quantityBd==0) "нет" else quantityBd.toString()} \nЗапланированые события: ${if(event==0) "нет" else quantityBd.toString()}"
        var notification_id = 1
        val notificationBuilderLow =
            NotificationCompat.Builder(context, channel).apply {
                setSmallIcon(R.drawable.ic_push)
                setContentTitle("PerfectDay")
                setContentText(result)
                setContentIntent(contextIntent)
                setAutoCancel(true)
                priority = NotificationManager.IMPORTANCE_LOW
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameLow = "Name $channel"
            val channelDescriptionLow = "Description: $channel"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow =
                NotificationChannel(
                    channel,
                    channelNameLow,
                    channelPriorityLow
                ).apply {
                    description = channelDescriptionLow
                }
            notificationManager.createNotificationChannel(channelLow)
        }
        notificationManager.notify(
            notification_id++,
            notificationBuilderLow.build()
        )
    }

    fun sendPush(listPush: List<DataPush>, channel: String) {

        if (listPush.isNotEmpty()) {

            var notification_id = 1
            listPush.forEach {
                val notificationBuilderLow =
                    NotificationCompat.Builder(context, channel).apply {
                        setSmallIcon(R.drawable.ic_push)
                        setContentTitle(it.title)
                        setContentText(it.message)
                        setContentIntent(contextIntent)
                        setAutoCancel(true)
                        priority = NotificationManager.IMPORTANCE_LOW
                    }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channelNameLow = "Name $channel"
                    val channelDescriptionLow = "Description: $channel"
                    val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
                    val channelLow =
                        NotificationChannel(
                            channel,
                            channelNameLow,
                            channelPriorityLow
                        ).apply {
                            description = channelDescriptionLow
                        }
                    notificationManager.createNotificationChannel(channelLow)
                }

                notificationManager.notify(
                    notification_id++,
                    notificationBuilderLow.build()
                )
            }

        }
    }


    private suspend fun getBD(onchangeDataBD: OnchangeDataBD) {
        val bd = withContext(Dispatchers.IO) {
            async {
                BirthdayFromPhoneInteractorImpl(context).getContactsByDay(localDate = LocalDate.now())
            }.await()
        }.map { DataPush(CHANNEL_BIRTHDAY, it.name, it.age.toString()) }

        onchangeDataBD.onChange(bd)
    }

    private suspend fun getEvent(onchangeDataBD: OnchangeDataBD) {
        val bd = scheduledEventIterctor.getScheduledEventsByDate(LocalDate.now()).map {
            DataPush(
                CHANNEL_EVENT, it.name, it.description
            )
        }
        onchangeDataBD.onChange(bd)
    }

    interface OnchangeDataBD {
        fun onChange(lisResult: List<DataPush>)
    }

}

package jt.projects.perfectday.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import jt.projects.model.DataModel
import jt.projects.perfectday.R
import jt.projects.perfectday.core.GlobalViewModel
import jt.projects.perfectday.presentation.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import java.time.LocalDate
import java.util.UUID

class PushService : KoinComponent {

    private val context: Context = get<Context>().applicationContext

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val globalViewModel: GlobalViewModel by inject<GlobalViewModel>()

    companion object {

        const val CHANNEL_PERFECT = "channel perfect day"
        const val TAG = "push_@"

        const val WORK_ID = "psh_work_manager"
        const val ID_DATA = "time_start"

        val uuidWork = UUID.randomUUID()

        fun newInstance(): PushService {
            return PushService()

        }
    }

    fun startPush() {

        CoroutineScope(Dispatchers.Default).launch {
            globalViewModel.getResultRecyclerByPeriod(LocalDate.now(), LocalDate.now())
                .collect {
                    val notesCount = it.filterIsInstance<DataModel.ScheduledEvent>().size
                    val birthdayCount = it.filterIsInstance<DataModel.BirthdayFromPhone>().size +
                            it.filterIsInstance<DataModel.BirthdayFromVk>().size
                    println("notesCount=$notesCount birthdayCount=$birthdayCount")
                    sendPush(birthdayCount, notesCount, CHANNEL_PERFECT)
                }
        }

    }

    private fun sendPush(countBd: Int, countNote: Int, channel: String) {
        val drText = context.getString(R.string.birthdays)
        val eventsText = context.getString(R.string.scheduled_vents)
        val no = context.getString(R.string.no)

        val result =
            "$drText: ${if (countBd == 0) "$no" else countBd.toString()} \n$eventsText: ${if (countNote == 0) "$no" else countNote.toString()}"
        var notification_id = 1
        val notificationBuilderLow =
            NotificationCompat.Builder(context, channel).apply {
                setSmallIcon(R.drawable.ic_push)
                setContentTitle("PerfectDay")
                setContentText(result)
                setContentIntent(getIntent())
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

    private fun getIntent(): PendingIntent {
        val intent = Intent(context?.applicationContext, MainActivity::class.java)
        val contextIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        return contextIntent
    }

}

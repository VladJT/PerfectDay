package jt.projects.perfectday.core.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import jt.projects.utils.LOG_TAG
import org.koin.java.KoinJavaComponent

class PdFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        // FireStore Cloud Messaging
        const val NOTIFICATION_KEY_TITLE = "title"
        const val NOTIFICATION_KEY_MESSAGE = "body"
    }

    private val notificationProvider by lazy {
        KoinJavaComponent.getKoin().get<NotificationProvider>()
    }

    override fun onNewToken(token: String) {
        Log.d(LOG_TAG, "Refreshed token: $token") // вызывается 1 раз !!
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(LOG_TAG, message.toString())
        val data = message.data
        val title = data[NOTIFICATION_KEY_TITLE]
        val body = data[NOTIFICATION_KEY_MESSAGE]
        if (!title.isNullOrEmpty() && !body.isNullOrEmpty()) {
            notificationProvider.send(title, body)
        }
        super.onMessageReceived(message)
    }
}
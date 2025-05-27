package com.example.pinjemfinandroid.Network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.pinjemfinandroid.Local.NotificationRepository
import com.example.pinjemfinandroid.Local.entity.NotificationEntity
import com.example.pinjemfinandroid.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MyFirebaseMessagingService: FirebaseMessagingService() {
    @Inject
    lateinit var notificationRepository: NotificationRepository
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCM","From: ${message.from}")
        if (message.data.isNotEmpty()) {
            Log.d("FCM","Message data payload: ${message.data}")
            showNotification(message.data["title"], message.data["body"])

            CoroutineScope(Dispatchers.IO).launch {
                val notification = message.data["title"]?.let { title ->
                    message.data["body"]?.let { body ->
                        NotificationEntity(
                            title = title,
                            body = body,
                            createdAt = System.currentTimeMillis(),
                            isRead = false
                        )
                    }
                }
                if (notification != null) {
                    notificationRepository.insertNotification(notification)
                }
            }
        } else if (message.notification != null) {
            val notif = message.notification!!
            Log.d("FCM","Message Notification Body: ${notif.body}")
            showNotification(notif.title, notif.body)

            CoroutineScope(Dispatchers.IO).launch {
                val notification = NotificationEntity(
                    title = notif.title ?: "Notifikasi",
                    body = notif.body ?: "",
                    createdAt = System.currentTimeMillis(),
                    isRead = false
                )
                notificationRepository.insertNotification(notification)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM","Refreshed token: $token")
    }

    private fun showNotification(title: String?, message: String?) {
        val channelId = "default_channel_id_v4"
        val channelName = "Default Channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val soundUri = Uri.parse("android.resource://${packageName}/raw/notif2")
        // Buat channel (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(soundUri, audioAttributes)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo) // ganti dengan ikonmu
            .setContentTitle(title ?: "Notifikasi")
            .setContentText(message ?: "")
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }
}
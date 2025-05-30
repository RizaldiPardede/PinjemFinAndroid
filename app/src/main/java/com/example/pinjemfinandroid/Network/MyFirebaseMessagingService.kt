package com.example.pinjemfinandroid.Network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.pinjemfinandroid.Local.NotificationRepository
import com.example.pinjemfinandroid.Local.entity.NotificationEntity
import com.example.pinjemfinandroid.R
import com.example.pinjemfinandroid.Utils.SaveNotificationWorker
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MyFirebaseMessagingService: FirebaseMessagingService() {
    @Inject
    lateinit var notificationRepository: NotificationRepository
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = Intent("refresh-ui")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        Log.d("FCM","From: ${message.from}")

        if (message.data.isNotEmpty()) {
            Log.d("FCM", "Repo: $notificationRepository")
            Log.d("FCM","Message data payload: ${message.data}")
            showNotification(message.data["title"], message.data["body"])

            val title = message.data["title"] ?: "Notifikasi"
            val body = message.data["body"] ?: ""

            val workRequest = OneTimeWorkRequestBuilder<SaveNotificationWorker>()
                .setInputData(workDataOf(
                    "title" to title,
                    "body" to body
                )).setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

            WorkManager.getInstance(this).enqueue(workRequest)
        } else if (message.notification != null) {
            val notif = message.notification!!
            Log.d("FCM", "Repo: $notificationRepository")
            Log.d("FCM","Message Notification Body: ${notif.body}")
            showNotification(notif.title, notif.body)

            val title = notif.title ?: "Notifikasi"
            val body = notif.body ?: ""

            val workRequest = OneTimeWorkRequestBuilder<SaveNotificationWorker>()
                .setInputData(workDataOf(
                    "title" to title,
                    "body" to body
                )).setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

            WorkManager.getInstance(this).enqueue(workRequest)
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
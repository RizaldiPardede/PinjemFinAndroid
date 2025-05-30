package com.example.pinjemfinandroid.Utils

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.pinjemfinandroid.Local.NotificationRepository
import com.example.pinjemfinandroid.Local.entity.NotificationEntity
import com.example.pinjemfinandroid.MyApplication
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

class SaveNotificationWorker(
    context: Context,
    params: WorkerParameters,
    // repository tidak di-inject, tapi buat manual
) : CoroutineWorker(context, params) {

    // Buat repository manual, contoh via singleton atau objek statis
    private val repository = NotificationRepository.getInstance(context)

    override suspend fun doWork(): Result {
        Log.d("SaveNotificationWorker", "Starting work")
        val title = inputData.getString("title") ?: return Result.failure()
        val body = inputData.getString("body") ?: return Result.failure()

        val notif = NotificationEntity(
            title = title,
            body = body,
            createdAt = System.currentTimeMillis(),
            isRead = false
        )

        return try {
            Log.d("SaveNotificationWorker", "Inserting notification: $notif")
            repository.insertNotification(notif)
            Log.d("SaveNotificationWorker", "Insert success")
            Result.success()
        } catch (e: Exception) {
            Log.e("SaveNotificationWorker", "Insert failed", e)
            Result.retry()
        }
    }
}
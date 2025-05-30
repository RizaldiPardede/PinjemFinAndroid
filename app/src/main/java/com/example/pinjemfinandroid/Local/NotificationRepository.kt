package com.example.pinjemfinandroid.Local

import android.content.Context
import com.example.pinjemfinandroid.Local.dao.NotificationDao
import com.example.pinjemfinandroid.Local.database.AppDatabase
import com.example.pinjemfinandroid.Local.entity.NotificationEntity
import java.util.concurrent.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao
) {
    companion object {
        @Volatile
        private var INSTANCE: NotificationRepository? = null

        fun getInstance(context: Context): NotificationRepository {
            return INSTANCE ?: synchronized(this) {
                val database = AppDatabase.getInstance(context)  // pastikan kamu punya getInstance di AppDatabase
                val instance = NotificationRepository(database.notificationDao())
                INSTANCE = instance
                instance
            }
        }
    }

    // Insert notifikasi baru
    suspend fun insertNotification(notification: NotificationEntity) {
        notificationDao.insert(notification)
    }

    // Update notifikasi sudah dibaca
    suspend fun markAsRead(id: Int) {
        notificationDao.markAsRead(id)
    }

    // Hapus semua notifikasi
    suspend fun deleteAllNotifications() {
        notificationDao.deleteAll()
    }


}
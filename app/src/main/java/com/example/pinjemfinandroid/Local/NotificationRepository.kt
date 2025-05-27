package com.example.pinjemfinandroid.Local

import com.example.pinjemfinandroid.Local.dao.NotificationDao
import com.example.pinjemfinandroid.Local.entity.NotificationEntity
import java.util.concurrent.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao
) {

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
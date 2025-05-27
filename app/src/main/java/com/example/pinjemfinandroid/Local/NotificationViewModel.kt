package com.example.pinjemfinandroid.Local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinjemfinandroid.Local.dao.NotificationDao
import com.example.pinjemfinandroid.Local.entity.NotificationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor (private val notificationDao: NotificationDao): ViewModel(){
    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount

    init {
        getAllNotifications()
        refreshUnreadCount()
    }

    fun insertNotification(title: String, body: String) {
        viewModelScope.launch {
            val notification = NotificationEntity(
                title = title,
                body = body
            )
            notificationDao.insert(notification)
            getAllNotifications() // refresh list
        }
    }

    fun markAsRead(id: Int) {
        viewModelScope.launch {
            notificationDao.markAsRead(id)
            getAllNotifications()
        }
    }

    fun deleteAllNotifications() {
        viewModelScope.launch {
            notificationDao.deleteAll()
            getAllNotifications()
        }
    }

    private fun getAllNotifications() {
        viewModelScope.launch {
            _notifications.value = notificationDao.getAll()
        }
    }
    fun markAllAsRead() {
        viewModelScope.launch {
            notificationDao.markAllAsRead()
            getAllNotifications() // refresh list setelah update
        }
    }

    fun refreshUnreadCount() {
        viewModelScope.launch {
            _unreadCount.value = notificationDao.countUnread()
        }
    }

}
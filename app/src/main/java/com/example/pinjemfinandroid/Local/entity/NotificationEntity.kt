package com.example.pinjemfinandroid.Local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val body: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

package com.example.pinjemfinandroid.Local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pinjemfinandroid.Local.dao.NotificationDao
import com.example.pinjemfinandroid.Local.dao.UserDataDao
import com.example.pinjemfinandroid.Local.entity.NotificationEntity
import com.example.pinjemfinandroid.Local.entity.UserData

@Database(entities = [UserData::class, NotificationEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDataDao(): UserDataDao
    abstract fun notificationDao(): NotificationDao
}
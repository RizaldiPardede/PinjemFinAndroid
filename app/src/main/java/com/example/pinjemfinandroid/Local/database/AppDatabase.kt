package com.example.pinjemfinandroid.Local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pinjemfinandroid.Local.dao.NotificationDao
import com.example.pinjemfinandroid.Local.dao.UserDataDao
import com.example.pinjemfinandroid.Local.entity.NotificationEntity
import com.example.pinjemfinandroid.Local.entity.UserData

@Database(entities = [UserData::class, NotificationEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDataDao(): UserDataDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
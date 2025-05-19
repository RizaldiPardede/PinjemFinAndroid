package com.example.pinjemfinandroid.Local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pinjemfinandroid.Local.entity.UserData


@Dao
interface UserDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userData: UserData)

    @Query("SELECT * FROM user_data")
    suspend fun getAllUsers(): List<UserData>

    @Query("DELETE FROM user_data")
    suspend fun clearAll()
}
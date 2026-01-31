package com.example.iotestapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.iotestapp.data.local.dao.UserDao
import com.example.iotestapp.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "app_database"
    }

    abstract fun userDao() : UserDao
}
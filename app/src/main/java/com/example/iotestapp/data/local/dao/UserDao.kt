package com.example.iotestapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.iotestapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun loginUser(user: UserEntity)

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getLoginUser(): UserEntity?

    @Query("DELETE FROM user")
    suspend fun clearUser()
}
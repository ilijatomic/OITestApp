package com.example.iotestapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.iotestapp.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun loginUser(user: UserEntity): Long

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: Long): UserEntity?

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getLoginUser(): UserEntity?

    @Query("DELETE FROM user")
    suspend fun clearUser()
}
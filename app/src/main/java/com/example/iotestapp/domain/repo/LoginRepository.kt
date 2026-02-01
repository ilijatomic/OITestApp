package com.example.iotestapp.domain.repo

import com.example.iotestapp.data.local.entity.UserEntity
import com.example.iotestapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(user: User): User?
    suspend fun checkLogin(): Boolean
    suspend fun logout()
}
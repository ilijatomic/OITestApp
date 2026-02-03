package com.example.iotestapp.domain.repo

import com.example.iotestapp.domain.model.User

interface LoginRepository {
    suspend fun login(user: User): User?
    suspend fun getLoginUser(): User?
    suspend fun logout(): Boolean
}
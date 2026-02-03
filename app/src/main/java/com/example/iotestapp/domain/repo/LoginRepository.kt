package com.example.iotestapp.domain.repo

import com.example.iotestapp.domain.model.User

interface LoginRepository {
    /**
     * Login user if matching credentials
     * @param User username and password
     */
    suspend fun login(user: User): User?

    /**
     * Gets active user in DB
     * @return found [User] or null if no user is logged in
     */
    suspend fun getLoginUser(): User?

    /**
     * Clears active logged in user
     * @return success flag
     */
    suspend fun logout(): Boolean
}
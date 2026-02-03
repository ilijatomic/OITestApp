package com.example.iotestapp.data.repository

import com.example.iotestapp.data.local.dao.UserDao
import com.example.iotestapp.domain.mappers.toDomain
import com.example.iotestapp.domain.mappers.toEntity
import com.example.iotestapp.domain.model.User
import com.example.iotestapp.domain.repo.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : LoginRepository {

    override suspend fun login(user: User): User? {
        if (user.username != "admin" || user.password != "admin") {
            return null
        }
        val id = userDao.loginUser(user.toEntity())
        return userDao.getUserById(id)?.toDomain()
    }

    override suspend fun getLoginUser(): User? {
        return userDao.getLoginUser()?.toDomain()
    }

    override suspend fun logout(): Boolean {
        return getLoginUser()?.let {
            userDao.clearUser()
            true
        } ?: false
    }
}
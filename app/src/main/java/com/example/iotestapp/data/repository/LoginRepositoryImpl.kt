package com.example.iotestapp.data.repository

import com.example.iotestapp.data.local.dao.UserDao
import com.example.iotestapp.data.local.entity.UserEntity
import com.example.iotestapp.domain.mappers.toEntity
import com.example.iotestapp.domain.model.User
import com.example.iotestapp.domain.repo.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : LoginRepository {

    override suspend fun login(user: User): UserEntity? {
        if (user.username != "admin" || user.password != "admin") {
            return null
        }
        val id = userDao.loginUser(user.toEntity())
        return userDao.getUserById(id)
    }

    override suspend fun checkLogin(): Boolean {
        return userDao.getLoginUser() != null
    }

    override suspend fun logout() {
        return userDao.clearUser()
    }
}
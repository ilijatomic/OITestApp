package com.example.iotestapp.domain.usecase.login

import com.example.iotestapp.R
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.User
import com.example.iotestapp.domain.repo.LoginRepository
import javax.inject.Inject

/**
 * Use case for trying to login user
 */
class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(username: String, password: String): Resource<User> {
        try {
            if (username.isBlank() || password.isBlank()) {
                return Resource.Error(R.string.login_error_username_empty)
            }
            val user = loginRepository.login(User(username, password))
            user?.let {
                return Resource.Success(user)
            }
            return Resource.Error(R.string.login_error_username_incorrect)
        } catch (e: Exception) {
            return Resource.Error(message = e.message.toString())
        }
    }
}
package com.example.iotestapp.domain.usecase.login

import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.User
import com.example.iotestapp.domain.repo.LoginRepository
import javax.inject.Inject

/**
 * Use case for getting logged in user
 */
class GetLoginUserUseCase @Inject constructor(
    private val loginRepository: LoginRepository
){
    suspend operator fun invoke() : Resource<User?> {
        return try {
            Resource.Success(loginRepository.getLoginUser())
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}
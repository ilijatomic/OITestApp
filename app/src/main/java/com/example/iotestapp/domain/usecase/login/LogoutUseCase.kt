package com.example.iotestapp.domain.usecase.login

import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.repo.LoginRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val loginRepository: LoginRepository
){
    suspend operator fun invoke() : Resource<Unit> {
        return try {
            Resource.Success(loginRepository.logout())
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}
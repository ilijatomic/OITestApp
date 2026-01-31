package com.example.iotestapp.domain.usecase.login

import com.example.iotestapp.domain.repo.LoginRepository
import javax.inject.Inject

class CheckLoggedUseCase @Inject constructor(
    private val loginRepository: LoginRepository
){
    suspend operator fun invoke() : Result<Boolean> {
        return try {
            Result.success(loginRepository.checkLogin())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
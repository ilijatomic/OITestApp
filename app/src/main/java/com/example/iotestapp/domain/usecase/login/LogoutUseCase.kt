package com.example.iotestapp.domain.usecase.login

import com.example.iotestapp.domain.repo.LoginRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val loginRepository: LoginRepository
){
    suspend operator fun invoke() : Result<Unit> {
        try {
            loginRepository.logout()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
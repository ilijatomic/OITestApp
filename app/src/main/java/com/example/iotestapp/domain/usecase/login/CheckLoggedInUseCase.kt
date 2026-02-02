package com.example.iotestapp.domain.usecase.login

import android.content.res.Resources
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.User
import com.example.iotestapp.domain.repo.LoginRepository
import javax.inject.Inject

class CheckLoggedInUseCase @Inject constructor(
    private val loginRepository: LoginRepository
){
    suspend operator fun invoke() : Resource<User?> {
        return try {
            Resource.Success(loginRepository.checkLogin())
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}
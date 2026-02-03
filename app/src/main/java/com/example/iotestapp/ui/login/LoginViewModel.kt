package com.example.iotestapp.ui.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.User
import com.example.iotestapp.domain.usecase.login.GetLoginUserUseCase
import com.example.iotestapp.domain.usecase.login.LoginUseCase
import com.example.iotestapp.ui.common.BaseViewModel
import com.example.iotestapp.ui.common.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getLoginUserUseCase: GetLoginUserUseCase,
) : BaseViewModel() {

    private val _loginState = MutableStateFlow<ViewModelState<User?>>(LoginState.Checking)
    val loginState = _loginState.asStateFlow()

    fun checkIfUserIsLoggedIn() {
        viewModelScope.launch {
            val result = getLoginUserUseCase.invoke()
            Log.d(TAG, "checkIfUserIsLoggedIn: $result")
            delay(2000) // simulating check user time
            when (result) {
                is Resource.Success<*> -> {
                    result.data?.let {
                        _loginState.value = ViewModelState.Result(it)
                    } ?: run {
                        _loginState.value = LoginState.Idle
                    }
                }

                is Resource.Error<*> -> postError(result, _loginState)
            }
        }
    }

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = ViewModelState.Loading
            val result = loginUseCase.invoke(username, password)
            Log.d(TAG, "loginUser: $result")
            delay(2000) // simulating login time
            when (result) {
                is Resource.Success<*> -> _loginState.value = ViewModelState.Result(result.data)
                is Resource.Error<*> -> postError(result, _loginState)

            }
        }
    }

    sealed class LoginState : ViewModelState<User?>() {
        data object Checking : LoginState()
        data object Idle : LoginState()
    }
}


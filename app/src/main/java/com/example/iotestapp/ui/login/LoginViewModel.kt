package com.example.iotestapp.ui.login

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iotestapp.R
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.usecase.login.CheckLoggedUseCase
import com.example.iotestapp.domain.usecase.login.LoginUseCase
import com.example.iotestapp.domain.usecase.login.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val checkLoggedUseCase: CheckLoggedUseCase,
) : ViewModel() {
    companion object {
        const val TAG = "LoginViewModel"
    }

    private val _loginState = MutableStateFlow<State>(State.Checking)
    val loginState = _loginState.asStateFlow()

    init {
        checkIfUserIsLoggedIn()
    }

    fun checkIfUserIsLoggedIn() {
        viewModelScope.launch {
            delay(3000) // simulating check user time
            checkLoggedUseCase.invoke()
                .onSuccess {
                    if (it) {
                        _loginState.value = State.Success
                    } else {
                        _loginState.value = State.Idle
                    }
                }
                .onFailure {
                    Log.e(TAG, it.message.toString())
                    _loginState.value = State.Error()
                }
        }
    }

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            viewModelScope.launch {
                _loginState.value = State.Loading
                val result = loginUseCase.invoke(username, password)
                delay(3000) // simulating login time
                when (result) {
                    is Resource.Success<*> -> _loginState.value = State.Success
                    is Resource.Error<*> -> _loginState.value = State.Error(result.id)
                    is Resource.Exception<*> -> {
                        Log.e(TAG, result.message.toString())
                        _loginState.value = State.Error()
                    }
                }
            }
        }
    }

    sealed class State {
        data object Checking : State()
        data object Idle : State()
        data object Loading : State()
        data object Success : State()
        data class Error(@StringRes val id: Int? = R.string.exception_error) : State()
    }
}


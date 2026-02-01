package com.example.iotestapp.ui.login

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iotestapp.R
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.usecase.login.CheckLoggedInUseCase
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
    private val checkLoggedInUseCase: CheckLoggedInUseCase,
) : BaseViewModel() {
    companion object {
        const val TAG = "LoginViewModel"
    }

    private val _loginState = MutableStateFlow<ViewModelState<Unit>>(State.Checking)
    val loginState = _loginState.asStateFlow()

    init {
        checkIfUserIsLoggedIn()
    }

    fun checkIfUserIsLoggedIn() {
        viewModelScope.launch {
            delay(2000) // simulating check user time
            when (val result = checkLoggedInUseCase.invoke()) {
                is Resource.Success<*> -> {
                    if (result.data == true) {
                        _loginState.value = ViewModelState.Result(Unit)
                    } else {
                        _loginState.value = State.Idle
                    }
                }
                is Resource.Error<*> -> postError(result)
            }
        }
    }

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            viewModelScope.launch {
                _loginState.value = ViewModelState.Loading
                val result = loginUseCase.invoke(username, password)
                delay(2000) // simulating login time
                when (result) {
                    is Resource.Success<*> -> _loginState.value = ViewModelState.Result(Unit)
                    is Resource.Error<*> -> postError(result)

                }
            }
        }
    }

    override fun postError(error: Resource.Error<*>) {
        error.id?.let {
            _loginState.value = ViewModelState.Error(it)
        }
        error.message?.let {
            Log.e(TAG, it)
            _loginState.value = ViewModelState.Error()
        }
    }

    sealed class State : ViewModelState<Unit>() {
        data object Checking : State()
        data object Idle : State()
    }
}


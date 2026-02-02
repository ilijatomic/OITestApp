package com.example.iotestapp.ui.navigation

import androidx.lifecycle.viewModelScope
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.User
import com.example.iotestapp.domain.usecase.login.GetLoggedInUserUseCase
import com.example.iotestapp.domain.usecase.login.LogoutUseCase
import com.example.iotestapp.ui.common.BaseViewModel
import com.example.iotestapp.ui.common.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel() {
    companion object {
        const val TAG = "NavigationViewModel"
    }

    private val _loginState = MutableStateFlow<ViewModelState<User?>>(ViewModelState.Loading)
    val loginState = _loginState.asStateFlow()

    private val _logoutState = MutableStateFlow<ViewModelState<Boolean>>(LogoutState.Idle)
    val logoutState = _logoutState.asStateFlow()

    init {
        getLoggedInUser()
    }

    fun getLoggedInUser() {
        viewModelScope.launch {
            when (val result = getLoggedInUserUseCase.invoke()) {
                is Resource.Success<*> -> {
                    _loginState.value = ViewModelState.Result(result.data)
                    _logoutState.value = LogoutState.Idle
                }
                is Resource.Error<*> -> postError(result, _loginState)
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            _logoutState.value = ViewModelState.Loading
            val result = logoutUseCase.invoke()
            when (result) {
                is Resource.Success<*> -> {
                    _logoutState.value = ViewModelState.Result(true)
                    _loginState.value = ViewModelState.Loading
                }
                is Resource.Error<*> -> postError(result, _logoutState)

            }
        }
    }

    fun resetLogoutState() {
        _logoutState.value = LogoutState.Idle
    }

    sealed class LogoutState : ViewModelState<Boolean>() {
        data object Idle : LogoutState()
    }
}


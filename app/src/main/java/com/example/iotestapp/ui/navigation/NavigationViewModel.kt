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

    private val _loginUser = MutableStateFlow<ViewModelState<User?>>(ViewModelState.Loading)
    val loginUser = _loginUser.asStateFlow()

    private val _logoutUser = MutableStateFlow<ViewModelState<Boolean>>(LogoutState.Idle)
    val logoutUser = _logoutUser.asStateFlow()

    init {
        getLoggedInUser()
    }

    fun getLoggedInUser() {
        viewModelScope.launch {
            when (val result = getLoggedInUserUseCase.invoke()) {
                is Resource.Success<*> -> {
                    _loginUser.value = ViewModelState.Result(result.data)
                    _logoutUser.value = LogoutState.Idle
                }
                is Resource.Error<*> -> postError(result, _loginUser)
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            _logoutUser.value = ViewModelState.Loading
            val result = logoutUseCase.invoke()
            when (result) {
                is Resource.Success<*> -> {
                    _logoutUser.value = ViewModelState.Result(true)
                    _loginUser.value = ViewModelState.Loading
                }
                is Resource.Error<*> -> postError(result, _logoutUser)

            }
        }
    }

    fun resetLogoutState() {
        _logoutUser.value = LogoutState.Idle
    }

    sealed class LogoutState : ViewModelState<Boolean>() {
        data object Idle : LogoutState()
    }
}


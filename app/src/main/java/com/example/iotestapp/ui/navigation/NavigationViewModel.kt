package com.example.iotestapp.ui.navigation

import androidx.lifecycle.viewModelScope
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.User
import com.example.iotestapp.domain.usecase.login.GetLoginUserUseCase
import com.example.iotestapp.domain.usecase.login.LogoutUseCase
import com.example.iotestapp.ui.common.BaseViewModel
import com.example.iotestapp.ui.common.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val getLoginUserUseCase: GetLoginUserUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel() {
    companion object {
        const val TAG = "NavigationViewModel"
    }

    private val _navLoginState = MutableStateFlow<ViewModelState<User?>>(NavLoginState.Idle)
    val navLoginState = _navLoginState.asStateFlow()

    private val _navLogoutState = MutableStateFlow<ViewModelState<Boolean>>(NavLogoutState.Idle)
    val navLogoutState = _navLogoutState.asStateFlow()

    fun getLoggedInUser() {
        viewModelScope.launch {
            when (val result = getLoginUserUseCase.invoke()) {
                is Resource.Success<*> -> {
                    _navLoginState.value = ViewModelState.Result(result.data)
                    _navLogoutState.value = NavLogoutState.Idle
                }
                is Resource.Error<*> -> postError(result, _navLoginState)
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            _navLogoutState.value = ViewModelState.Loading
            val result = logoutUseCase.invoke()
            delay(2000) // Simulate delay for logout
            when (result) {
                is Resource.Success<*> -> {
                    _navLogoutState.value = ViewModelState.Result(true)
                    _navLoginState.value = NavLoginState.Idle
                }
                is Resource.Error<*> -> postError(result, _navLogoutState)

            }
        }
    }

    fun resetLogoutState() {
        _navLogoutState.value = NavLogoutState.Idle
    }

    sealed class NavLoginState : ViewModelState<User?>() {
        data object Idle : NavLoginState()
    }

    sealed class NavLogoutState : ViewModelState<Boolean>() {
        data object Idle : NavLogoutState()
    }
}


package com.example.iotestapp.viewmodel.login

import app.cash.turbine.test
import com.example.iotestapp.R
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.usecase.login.GetLoginUserUseCase
import com.example.iotestapp.domain.usecase.login.LoginUseCase
import com.example.iotestapp.resources.UserMocks
import com.example.iotestapp.ui.common.ViewModelState
import com.example.iotestapp.ui.login.LoginViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LoginViewModel
    private val loginUseCase = mockk<LoginUseCase>(relaxed = true)
    private val getLoginUserUseCase = mockk<GetLoginUserUseCase>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = LoginViewModel(
            loginUseCase,
            getLoginUserUseCase
        )
    }

    @Test
    fun `check login with user when already logged in`() = runTest {
        val mockUser = UserMocks.validUser
        coEvery { getLoginUserUseCase.invoke() } returns Resource.Success(mockUser)

        viewModel.loginState.test {
            assertEquals(LoginViewModel.LoginState.Checking, awaitItem())
            viewModel.checkIfUserIsLoggedIn()
            val finalState = awaitItem() as ViewModelState.Result
            assertEquals(mockUser, finalState.data)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { getLoginUserUseCase.invoke() }
    }

    @Test
    fun `check login when no user logged in`() = runTest {
        coEvery { getLoginUserUseCase.invoke() } returns Resource.Success(null)

        viewModel.loginState.test {
            assertEquals(LoginViewModel.LoginState.Checking, awaitItem())
            viewModel.checkIfUserIsLoggedIn()
            assertEquals(LoginViewModel.LoginState.Idle, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { getLoginUserUseCase.invoke() }
    }

    @Test
    fun `check login when returns error`() = runTest {
        coEvery { getLoginUserUseCase.invoke() } returns Resource.Error(message = "Error fetching user")

        viewModel.loginState.test {
            assertEquals(LoginViewModel.LoginState.Checking, awaitItem())
            viewModel.checkIfUserIsLoggedIn()
            assertEquals(ViewModelState.Error(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { getLoginUserUseCase.invoke() }
    }

    @Test
    fun `login User on success`() = runTest {
        val mockUser = UserMocks.validUser
        coEvery { loginUseCase.invoke(any(), any()) } returns Resource.Success(mockUser)

        viewModel.loginState.test {
            assertEquals(LoginViewModel.LoginState.Checking, awaitItem())
            viewModel.loginUser(mockUser.username, mockUser.password!!)
            assertEquals(ViewModelState.Loading, awaitItem())
            val finalState = awaitItem() as ViewModelState.Result
            assertEquals(mockUser, finalState.data)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { loginUseCase.invoke(mockUser.username, mockUser.password!!) }
    }

    @Test
    fun `login User wrong credentials on error`() = runTest {
        val mockUser = UserMocks.invalidUser
        coEvery { loginUseCase.invoke(any(), any()) } returns Resource.Error(R.string.login_error_username_incorrect)

        viewModel.loginState.test {
            assertEquals(LoginViewModel.LoginState.Checking, awaitItem())
            viewModel.loginUser(mockUser.username, mockUser.password!!)
            assertEquals(ViewModelState.Loading, awaitItem())
            assertEquals(ViewModelState.Error(R.string.login_error_username_incorrect), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { loginUseCase.invoke(mockUser.username, mockUser.password!!) }
    }
}

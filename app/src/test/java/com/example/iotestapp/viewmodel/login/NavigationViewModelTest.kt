package com.example.iotestapp.viewmodel.login

import app.cash.turbine.test
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.usecase.login.GetLoginUserUseCase
import com.example.iotestapp.domain.usecase.login.LogoutUseCase
import com.example.iotestapp.resources.UserMocks
import com.example.iotestapp.ui.common.ViewModelState
import com.example.iotestapp.ui.navigation.NavigationViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NavigationViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: NavigationViewModel
    private val getLoginUserUseCase = mockk<GetLoginUserUseCase>(relaxed = true)
    private val logoutUseCase = mockk<LogoutUseCase>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = NavigationViewModel(
            getLoginUserUseCase,
            logoutUseCase
        )
    }

    @Test
    fun `login user`() = runTest {
        val mockData = UserMocks.validUser
        coEvery { getLoginUserUseCase.invoke() } returns Resource.Success(mockData)

        viewModel.navLoginState.test {
            assertEquals(NavigationViewModel.NavLoginState.Idle, awaitItem())
            viewModel.getLoggedInUser()
            val finalState = awaitItem() as ViewModelState.Result
            assertEquals(mockData, finalState.data)
            assertEquals(NavigationViewModel.NavLogoutState.Idle, viewModel.navLogoutState.first())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { getLoginUserUseCase.invoke() }
    }

    @Test
    fun `login user error`() = runTest {
        coEvery { getLoginUserUseCase.invoke() } returns Resource.Error(message = "Error fetching user")

        viewModel.navLoginState.test {
            assertEquals(NavigationViewModel.NavLoginState.Idle, awaitItem())
            viewModel.getLoggedInUser()
            assertEquals(ViewModelState.Error(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `logout user`() = runTest {
        coEvery { getLoginUserUseCase.invoke() } returns Resource.Success(null)
        coEvery { logoutUseCase.invoke() } returns Resource.Success(true)

        viewModel.navLogoutState.test {
            assertEquals(NavigationViewModel.NavLogoutState.Idle, awaitItem())
            viewModel.getLoggedInUser()
            assertEquals(ViewModelState.Result(null), viewModel.navLoginState.first())
            viewModel.logoutUser()
            assertEquals(ViewModelState.Loading, awaitItem())
            assertEquals(ViewModelState.Result(true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { logoutUseCase.invoke() }
    }

    @Test
    fun `logout error`() = runTest {
        coEvery { getLoginUserUseCase.invoke() } returns Resource.Success(null)
        coEvery { logoutUseCase.invoke() } returns Resource.Error(message = "Logout failed")

        viewModel.navLogoutState.test {
            assertEquals(NavigationViewModel.NavLogoutState.Idle, awaitItem())
            viewModel.getLoggedInUser()
            assertEquals(ViewModelState.Result(null), viewModel.navLoginState.first())
            viewModel.logoutUser()
            assertEquals(ViewModelState.Loading, awaitItem())
            assertEquals(ViewModelState.Error(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `resetLogoutState sets logoutState to Idle`() = runTest {
        coEvery { getLoginUserUseCase.invoke() } returns Resource.Success(null)
        coEvery { logoutUseCase.invoke() } returns Resource.Success(true)

        viewModel.resetLogoutState()
        assertEquals(NavigationViewModel.NavLogoutState.Idle, viewModel.navLogoutState.first())
    }
}
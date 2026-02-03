package com.example.iotestapp.usecase.login

import com.example.iotestapp.R
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.User
import com.example.iotestapp.domain.repo.LoginRepository
import com.example.iotestapp.domain.usecase.login.LoginUseCase
import com.example.iotestapp.resources.UserMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginUseCaseTest {

    private lateinit var loginUseCase: LoginUseCase
    private val loginRepository = mockk<LoginRepository>(relaxed = true)

    @Before
    fun setUp() {
        loginUseCase = LoginUseCase(loginRepository)
    }

    @Test
    fun `returns success with user data when login succeeds`() = runTest {
        val mockUser = UserMocks.validUser
        coEvery { loginRepository.login(any()) } returns mockUser

        val result = loginUseCase.invoke(mockUser.username, mockUser.password!!)

        assertEquals(Resource.Success(mockUser), result)
        coVerify(exactly = 1) { loginRepository.login(mockUser) }
    }

    @Test
    fun `returns error when username or password is blank`() = runTest {
        val mockUser = UserMocks.emptyUser

        val result = loginUseCase.invoke(mockUser.username, mockUser.password!!)
        val expected = Resource.Error<User?>(R.string.login_error_username_empty)

        assertEquals(expected, result)
        coVerify(exactly = 0) { loginRepository.login(any()) }
    }

    @Test
    fun `returns error when user not found`() = runTest {
        val mockUser = UserMocks.invalidUser
        coEvery { loginRepository.login(any()) } returns null

        val result = loginUseCase.invoke(mockUser.username, mockUser.password!!)
        val expected = Resource.Error<User?>(R.string.login_error_username_incorrect)

        assertEquals(expected, result)
        coVerify(exactly = 1) { loginRepository.login(any()) }
    }

    @Test
    fun `returns error when repository throws exception`() = runTest {
        val mockUser = UserMocks.validUser
        val errorMessage = "Exception"
        coEvery { loginRepository.login(any()) } throws Exception(errorMessage)

        val result = loginUseCase.invoke(mockUser.username, mockUser.password!!)
        val expected = Resource.Error<User?>(message = errorMessage)

        assertEquals(expected, result)
        coVerify(exactly = 1) { loginRepository.login(any()) }
    }
}

package com.example.iotestapp.usecase.login

import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.repo.LoginRepository
import com.example.iotestapp.domain.usecase.login.LogoutUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogoutUseCaseTest {

    private lateinit var logoutUseCase: LogoutUseCase
    private val loginRepository = mockk<LoginRepository>(relaxed = true)

    @Before
    fun setUp() {
        logoutUseCase = LogoutUseCase(loginRepository)
    }

    @Test
    fun `returns success with true when logout succeeds`() = runTest {
        coEvery { loginRepository.logout() } returns true

        val result = logoutUseCase.invoke()

        assertEquals(Resource.Success(true), result)
        coVerify(exactly = 1) { loginRepository.logout() }
    }

    @Test
    fun `returns success with false when no user logged in`() = runTest {
        coEvery { loginRepository.logout() } returns false

        val result = logoutUseCase.invoke()

        assertEquals(Resource.Success(false), result)
        coVerify(exactly = 1) { loginRepository.logout() }
    }

    @Test
    fun `returns error when repository throws exception`() = runTest {
        val errorMessage = "Exception"
        coEvery { loginRepository.logout() } throws Exception(errorMessage)

        val result = logoutUseCase.invoke()
        val expected = Resource.Error<Boolean>(message = errorMessage)

        assertEquals(expected, result)
        coVerify(exactly = 1) { loginRepository.logout() }
    }
}

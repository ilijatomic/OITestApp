package com.example.iotestapp.domain

import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.repo.LoginRepository
import com.example.iotestapp.domain.usecase.login.GetLoginUserUseCase
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
class GetLoginUserUseCaseTest {

    private lateinit var getLoginUserUseCase: GetLoginUserUseCase
    private val loginRepository = mockk<LoginRepository>(relaxed = true)

    @Before
    fun setUp() {
        getLoginUserUseCase = GetLoginUserUseCase(loginRepository)
    }

    @Test
    fun `invoke returns success with user data`() = runTest {
        val mockUser = UserMocks.validUser
        coEvery { loginRepository.getLoginUser() } returns mockUser

        val result = getLoginUserUseCase.invoke()

        assertEquals(Resource.Success(mockUser), result)
        coVerify(exactly = 1) { loginRepository.getLoginUser() }
    }

    @Test
    fun `invoke returns error when user not found`() = runTest {
        coEvery { loginRepository.getLoginUser() } throws Exception()

        val result = getLoginUserUseCase.invoke()

        assertEquals(true, result is Resource.Error)
        coVerify(exactly = 1) { loginRepository.getLoginUser() }
    }

    @Test
    fun `invoke returns null when no user logged in`() = runTest {
        coEvery { loginRepository.getLoginUser() } returns null

        val result = getLoginUserUseCase.invoke()

        assertEquals(Resource.Success(null), result)
        coVerify(exactly = 1) { loginRepository.getLoginUser() }
    }
}
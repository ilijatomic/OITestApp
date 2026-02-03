package com.example.iotestapp.data

import com.example.iotestapp.data.local.dao.UserDao
import com.example.iotestapp.data.repository.LoginRepositoryImpl
import com.example.iotestapp.domain.mappers.toEntity
import com.example.iotestapp.domain.repo.LoginRepository
import com.example.iotestapp.resources.UserMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class LoginRepositoryTest: DBSetup() {

    private lateinit var spyDao: UserDao
    private lateinit var loginRepository: LoginRepository

    @Before
    override fun setUp() {
        super.setUp()
        spyDao = spyk(db.userDao())
        loginRepository = LoginRepositoryImpl(spyDao)
    }

    @Test
    fun `returns user when login successful`() = runTest {
        val mockUser = UserMocks.validUser

        val result = loginRepository.login(mockUser)?.copy(password = "admin")

        assertEquals(mockUser, result)
        coVerify(exactly = 1) { spyDao.loginUser(mockUser.toEntity()) }
    }

    @Test
    fun `returns null when user not found during login`() = runTest {
        val mockUser = UserMocks.invalidUser

        val result = loginRepository.login(mockUser)

        assertNull(result)
        coVerify(exactly = 0) { spyDao.loginUser(mockUser.toEntity()) }
    }

    @Test
    fun `returns stored user after login`() = runTest {
        val mockUser = UserMocks.validUser
        loginRepository.login(mockUser)

        val result = loginRepository.getLoginUser()

        assertEquals(mockUser.username, result?.username)
        coVerify(exactly = 1) { spyDao.loginUser(mockUser.toEntity()) }
        coVerify(exactly = 1) { spyDao.getLoginUser() }
    }

    @Test
    fun `returns null when no user logged in`() = runTest {
        val result = loginRepository.getLoginUser()

        assertNull(result)
        coVerify(exactly = 1) { spyDao.getLoginUser() }
    }

    @Test
    fun `logout clears user and returns true`() = runTest {
        val mockUser = UserMocks.validUser
        loginRepository.login(mockUser)

        val result = loginRepository.logout()

        assertEquals(true, result)
        coVerify(exactly = 1) { spyDao.loginUser(mockUser.toEntity()) }
        coVerify(exactly = 1) { spyDao.getLoginUser() }
        coVerify(exactly = 1) { spyDao.clearUser() }
    }

    @Test
    fun `logout returns false when no user logged in`() = runTest {
        val result = loginRepository.logout()

        assertEquals(false, result)
        coVerify(exactly = 1) { spyDao.getLoginUser() }
    }


    @Test
    fun `multiple get user verify dao called each time`() = runTest {
        val mockUser = UserMocks.validUser
        loginRepository.login(mockUser)
        loginRepository.getLoginUser()
        loginRepository.getLoginUser()

        coVerify(exactly = 1) { spyDao.loginUser(mockUser.toEntity()) }
        coVerify(exactly = 2) { spyDao.getLoginUser() }
    }
}

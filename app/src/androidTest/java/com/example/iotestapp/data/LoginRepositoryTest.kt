package com.example.iotestapp.domain.repo

import com.example.iotestapp.data.local.dao.UserDao
import com.example.iotestapp.data.repository.LoginRepositoryImpl
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class LoginRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userDao: UserDao

    private lateinit var loginRepository: LoginRepository

    @Before
    fun init() {
        hiltRule.inject()
        loginRepository = LoginRepositoryImpl(userDao)
    }

//    @Test
//    fun login_with_hilt_dao_success() = runTest {
//        val user = UserEntity("test@example.com", "pass")
//        userDao.insertUser(user)
//
//        val result = repository.login("test@example.com", "pass")
//
//        assert(result is Resource.Success)
//    }
//
//    @Test
//    fun `getLoginUser returns user when login successful`() = runTest {
//        val mockUser = UserMocks.validUser
//        coEvery { loginDataSource.getUser() } returns mockUser
//
//        val result = loginRepository.getLoginUser()
//
//        assertEquals(mockUser, result)
//        coVerify(exactly = 1) { loginDataSource.getUser() }
//
//    }
//
//    @Test
//    fun `getLoginUser returns null when user not found`() = runTest {
//        coEvery { loginDataSource.getUser() } returns null
//
//        val result = loginRepository.getLoginUser()
//
//        assertEquals(null, result)
//    }
//
//    @Test
//    fun `getLoginUser throws exception when data source fails`() = runTest {
//        coEvery { loginDataSource.getUser() } throws Exception("Network error")
//
//        try {
//            loginRepository.getLoginUser()
//        } catch (e: Exception) {
//            assertEquals("Network error", e.message)
//        }
//    }
//
//    @Test
//    fun `loginUser saves credentials and returns success`() = runTest {
//        val username = "admin"
//        val password = "admin"
//        val mockUser = UserMocks.validUser
//        coEvery { loginDataSource.login(username, password) } returns mockUser
//
//        val result = loginRepository.login(username, password)
//
//        assertEquals(mockUser, result)
//        coVerify(exactly = 1) { loginDataSource.login(username, password) }
//    }
//
//    @Test
//    fun `logout clears stored user`() = runTest {
//        coEvery { loginDataSource.logout() } returns Unit
//
//        loginRepository.logout()
//
//        coVerify(exactly = 1) { loginDataSource.logout() }
//    }
}
package com.example.iotestapp.login

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.iotestapp.R
import com.example.iotestapp.ktx.waitUntilNodeExists
import com.example.iotestapp.resources.UserMocks
import com.example.iotestapp.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginUiTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var loginLabel : String
    private lateinit var usernameLabel : String
    private lateinit var passwordLabel : String

    @Before
    fun setUp() {
        hiltRule.inject()

        loginLabel = composeTestRule.activity.getString(R.string.login)
        usernameLabel = composeTestRule.activity.getString(R.string.username)
        passwordLabel = composeTestRule.activity.getString(R.string.password)

        composeTestRule.waitUntilNodeExists(hasText(loginLabel))
    }

    @Test
    fun loginScreenIsDisplayedAfterLaunch() {
        composeTestRule.onNodeWithText(loginLabel).assertExists()
    }

    @Test
    fun loginFailsIfEmptyUsernameOrPassword() {
        val emptyUser = UserMocks.emptyUser

        composeTestRule.onNodeWithText(usernameLabel).performTextInput(emptyUser.username)
        composeTestRule.onNodeWithText(passwordLabel).performTextInput(emptyUser.password!!)
        composeTestRule.onNodeWithText(loginLabel).performClick()

        val errorMessage = composeTestRule.activity.getString(R.string.login_error_username_empty)

        composeTestRule.waitUntilNodeExists(hasText(errorMessage))
        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }

    @Test
    fun loginFailsIfWrongUsernameOrPassword() {
        val invalidUser = UserMocks.invalidUser

        composeTestRule.onNodeWithText(usernameLabel).performTextInput(invalidUser.username)
        composeTestRule.onNodeWithText(passwordLabel).performTextInput(invalidUser.password!!)
        composeTestRule.onNodeWithText(loginLabel).performClick()

        val errorMessage = composeTestRule.activity.getString(R.string.login_error_username_incorrect)

        composeTestRule.waitUntilNodeExists(hasText(errorMessage))
        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }

    @Test
    fun loginSuccess() {
        val validUser = UserMocks.validUser

        composeTestRule.onNodeWithText(usernameLabel).performTextInput(validUser.username)
        composeTestRule.onNodeWithText(passwordLabel).performTextInput(validUser.password!!)
        composeTestRule.onNodeWithText(loginLabel).performClick()

        val lowStockLabel = composeTestRule.activity.getString(R.string.dashboard_low_stock_title)

        composeTestRule.waitUntilNodeExists(hasText(lowStockLabel))
        composeTestRule.onNodeWithText(lowStockLabel).assertExists()
    }
}
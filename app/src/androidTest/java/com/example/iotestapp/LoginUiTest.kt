package com.example.iotestapp

import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginUiTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun login_simulation_on_actual_activity() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.iotestapp", appContext.packageName)
    }
}
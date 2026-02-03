package com.example.iotestapp.ktx

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.ComposeTestRule


private const val UI_ELEMENT_TIMEOUT = 5_000L

fun ComposeTestRule.waitUntilNodeExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = UI_ELEMENT_TIMEOUT
) {
    this.waitUntil(timeoutMillis) {
        this.onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
    }
}
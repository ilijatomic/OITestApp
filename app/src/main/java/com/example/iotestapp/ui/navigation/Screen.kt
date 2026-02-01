package com.example.iotestapp.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Support
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.iotestapp.R

sealed class Screen(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
) {
    object Login: Screen("login", R.string.login, Icons.AutoMirrored.Filled.Login)
    object Dashboard: Screen("dashboard", R.string.dashboard, Icons.Default.Home)
    object Suppliers: Screen("suppliers", R.string.suppliers, Icons.Default.Support)
    object Logout: Screen("logout", R.string.logout, Icons.AutoMirrored.Filled.Logout)
}

val drawerItems = listOf(Screen.Dashboard, Screen.Suppliers)
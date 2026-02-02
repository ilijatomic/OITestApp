package com.example.iotestapp.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.iotestapp.R

sealed class Screen(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
) {
    object Login: Screen("login", R.string.login, Icons.AutoMirrored.Filled.Login)
    object Dashboard: Screen("dashboard", R.string.dashboard, Icons.Default.Home)
    object Products: Screen("products", R.string.product_title, Icons.Default.ShoppingCart)
    object Suppliers: Screen("suppliers", R.string.supplier_title, Icons.Default.Support)
    object Transactions: Screen("transactions", R.string.transaction_title, Icons.Default.Receipt)
    object Logout: Screen("logout", R.string.logout, Icons.AutoMirrored.Filled.Logout)
}

val drawerItems = listOf(Screen.Dashboard, Screen.Products, Screen.Suppliers, Screen.Transactions)
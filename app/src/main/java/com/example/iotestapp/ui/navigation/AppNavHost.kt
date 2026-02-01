package com.example.iotestapp.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.iotestapp.R
import com.example.iotestapp.ui.common.HorizontalSpacerLarge
import com.example.iotestapp.ui.dashboard.DashboardScreen
import com.example.iotestapp.ui.login.LoginScreen
import com.example.iotestapp.ui.products.ProductsScreen
import com.example.iotestapp.ui.suppliers.SuppliersScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isLoginScreen = currentRoute == Screen.Login.route
    val screenTitle = when (currentRoute) {
        Screen.Suppliers.route -> Screen.Suppliers.title
        else -> R.string.app_name
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !isLoginScreen,
        drawerContent = {
            AppDrawerContent(navController, drawerState, scope)
        }
    ) {
        Scaffold(
            topBar = {
                if (!isLoginScreen) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = stringResource(screenTitle),
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Open Navigation Drawer"
                                )
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Login.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Login.route) {
                    LoginScreen {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
                composable(Screen.Dashboard.route) {
                    DashboardScreen()
                }
                composable(Screen.Products.route) {
                    ProductsScreen()
                }
                composable(Screen.Suppliers.route) {
                    SuppliersScreen()
                }
            }
        }
    }
}

@Composable
fun AppDrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalDrawerSheet {
        DrawerHeader(email = "user@example.com")

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        drawerItems.forEach {
            NavigationDrawerItem(
                label = { Text(stringResource(it.title)) },
                icon = { Icon(it.icon, contentDescription = null) },
                selected = currentRoute == it.route,
                onClick = {
                    scope.launch { drawerState.close() }
                    if (currentRoute != it.route) {
                        navController.navigate(it.route) {
                            popUpTo(navController.graph.id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }

                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

@Composable
fun DrawerHeader(email: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        HorizontalSpacerLarge()

        Text(
            text = email,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
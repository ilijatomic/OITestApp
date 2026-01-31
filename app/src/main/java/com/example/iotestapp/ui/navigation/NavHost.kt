package com.example.iotestapp.ui.navigation

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.iotestapp.ui.dashboard.DashboardScreen
import com.example.iotestapp.ui.login.LoginScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavHost() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isLoginScreen = currentRoute == Routes.LOGIN

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
                                text = "IOTestApp",
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
                startDestination = Routes.LOGIN,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Routes.LOGIN) {
                    LoginScreen {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                }
                composable(Routes.DASHBOARD) {
                    DashboardScreen()
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
        // 1. Header Section
        DrawerHeader(email = "user@example.com") // Replace with actual user data

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // 2. Navigation Items
        NavigationDrawerItem(
            label = { Text(text = "Dashboard") },
//                icon = { Icon(screen.icon, contentDescription = null) },
            selected = currentRoute == Routes.DASHBOARD,
            onClick = {
                scope.launch { drawerState.close() }
                if (currentRoute != Routes.DASHBOARD) {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.weight(1.0f)) // Pushes Logout to the bottom

//        // 3. Footer / Logout Section
//        NavigationDrawerItem(
//            label = { Text("Logout") },
//            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
//            selected = false,
//            onClick = {
//                scope.launch { drawerState.close() }
//                // Use popUpTo(0) to clear the entire backstack
//                navController.navigate(Screen.Login.route) {
//                    popUpTo(0) { inclusive = true }
//                }
//            },
//            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//        )
//        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun DrawerHeader(email: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {
        // Simple Profile Icon
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

        Spacer(modifier = Modifier.height(12.dp))

        // User Email Text
        Text(
            text = email,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
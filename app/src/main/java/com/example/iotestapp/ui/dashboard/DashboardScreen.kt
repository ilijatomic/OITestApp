package com.example.iotestapp.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.iotestapp.ui.common.HorizontalSpacerLarge
import com.example.iotestapp.ui.theme.dimen

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val lowStockState by viewModel.lowStockProductsState.collectAsState()
    val recentTransactionState by viewModel.recentTransactionsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLowStockProducts()
        viewModel.loadRecentTransactions()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimen.dashboardPadding),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.dashboardListsSpace),
        ) {
            item { HorizontalSpacerLarge() }

            LowStockProductsSection(state = lowStockState)

            item { HorizontalSpacerLarge() }

            RecentTransactionsSection(state = recentTransactionState)
        }
    }
}

package com.example.iotestapp.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.iotestapp.R
import com.example.iotestapp.ui.common.HorizontalSpacerSmall
import com.example.iotestapp.ui.common.ViewModelState
import com.example.iotestapp.ui.transactions.TransactionItem
import com.example.testapp.domain.model.Transaction

fun LazyListScope.RecentTransactionsSection(
    state: ViewModelState<List<Transaction>>,
) {
    item {
        Text(
            text = stringResource(R.string.dashboard_recent_transactions_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        HorizontalSpacerSmall()
    }

    when (state) {
        is ViewModelState.Loading -> item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is ViewModelState.Result -> {
            val transactions = state.data
            if (transactions.isEmpty()) {
                item { DashboardMessageCard(text = stringResource(R.string.dashboard_recent_transactions_empty)) }
            } else {
                items(items = transactions, key = { "${it.id}-${it.date}" }) {
                    TransactionItem(it)
                }
            }
        }
    }
}


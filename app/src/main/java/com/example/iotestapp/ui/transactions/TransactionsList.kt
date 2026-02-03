package com.example.iotestapp.ui.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.iotestapp.R
import com.example.iotestapp.ui.common.HorizontalSpacerSmall
import com.example.iotestapp.ui.common.formattedDate
import com.example.iotestapp.ui.theme.dimen
import com.example.testapp.domain.model.Transaction

@Composable
fun TransactionsList(
    transactions: List<Transaction>,
    emptyMessage: String,
) {
    if (transactions.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Text(
                text = emptyMessage,
                modifier = Modifier.padding(MaterialTheme.dimen.transactionListEmptyPadding),
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(bottom = MaterialTheme.dimen.transactionListPaddingB),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.transactionListSpace),
        ) {
            items(items = transactions, key = { it.id ?: "${it.product.id}-${it.date}-${it.type}-${it.quantity}" }) {
                TransactionItem(it)
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.dimen.transactionListItemPadding)
        ) {
            Text(
                text = "${transaction.type} • ${transaction.quantity}",
                style = MaterialTheme.typography.titleSmall,
            )
            HorizontalSpacerSmall()
            Text(
                text = "${stringResource(R.string.product)}: ${transaction.product.name}",
                style = MaterialTheme.typography.bodySmall,
            )
            HorizontalSpacerSmall()
            Text(
                text = formattedDate(transaction.date),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}




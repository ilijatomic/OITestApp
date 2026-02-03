package com.example.iotestapp.ui.suppliers

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import com.example.iotestapp.R
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.ui.common.HorizontalSpacerSmall
import com.example.iotestapp.ui.theme.dimen

@Composable
fun SuppliersList(
    suppliers: List<Supplier>,
    emptyMessage: String,
    onSupplierClick: (Supplier) -> Unit
) {
    if (suppliers.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                emptyMessage,
                modifier = Modifier.padding(MaterialTheme.dimen.supplierListEmptyPadding)
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(bottom = MaterialTheme.dimen.supplierListPaddingB),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.supplierListSpace)
        ) {
            items(items = suppliers, key = { it.id!! }) {
                SupplierItem(
                    supplier = it,
                    onClick = { onSupplierClick(it) }
                )
            }
        }
    }
}

@Composable
fun SupplierItem(
    supplier: Supplier,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimen.supplierListItemPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${stringResource(R.string.name)}: ${supplier.name}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            HorizontalSpacerSmall()
            Text(
                text = "${stringResource(R.string.contact_person)}: ${supplier.contactPerson}",
                style = MaterialTheme.typography.bodySmall,
            )
            HorizontalSpacerSmall()
            Text(
                text = "${stringResource(R.string.phone)}: ${supplier.phone}",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
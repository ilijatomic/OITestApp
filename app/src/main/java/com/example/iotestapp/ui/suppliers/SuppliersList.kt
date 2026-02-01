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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.iotestapp.R
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.ui.common.HorizontalSpacerSmall

@Composable
fun SuppliersList(
    suppliers: List<Supplier>,
    onSupplierClick: (Supplier) -> Unit
) {
    if (suppliers.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(text = stringResource(id = R.string.supplier_empty), modifier = Modifier.padding(24.dp))
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(bottom = 80.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = supplier.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            HorizontalSpacerSmall()
            Text(
                text = supplier.contactPerson,
                style = MaterialTheme.typography.bodySmall,
            )
            HorizontalSpacerSmall()
            Text(
                text = supplier.address,
                style = MaterialTheme.typography.bodySmall,
            )
            HorizontalSpacerSmall()
            Text(
                text = supplier.email,
                style = MaterialTheme.typography.bodySmall,
            )
            HorizontalSpacerSmall()
            Text(
                text = supplier.phone,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
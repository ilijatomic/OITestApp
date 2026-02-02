package com.example.iotestapp.ui.products

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
import androidx.compose.ui.text.font.FontWeight
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.ui.common.HorizontalSpacerSmall
import com.example.iotestapp.ui.theme.dimen

@Composable
fun ProductsList(
    products: List<Product>,
    emptyMessage: String,
    onProductClick: (Product) -> Unit,
) {
    if (products.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Text(
                text = emptyMessage,
                modifier = Modifier.padding(MaterialTheme.dimen.productListEmptyPadding),
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(bottom = MaterialTheme.dimen.productListPaddingB),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.productListSpace),
        ) {
            items(items = products, key = { it.id ?: it.barcode }) { product ->
                ProductItem(
                    product = product,
                    onClick = { onProductClick(product) },
                )
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimen.productListItemPadding),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
            )
            HorizontalSpacerSmall()
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodySmall
            )
            HorizontalSpacerSmall()
            Text(
                text = product.supplier.name,
                style = MaterialTheme.typography.bodySmall
            )
            HorizontalSpacerSmall()
            Text(
                text = product.currentStockLevel.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = if (product.currentStockLevel < product.minimumStockLevel) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }
    }
}


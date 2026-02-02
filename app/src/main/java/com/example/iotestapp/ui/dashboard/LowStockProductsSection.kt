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
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.ui.common.HorizontalSpacerSmall
import com.example.iotestapp.ui.common.ViewModelState
import com.example.iotestapp.ui.products.ProductItem

fun LazyListScope.LowStockProductsSection(
    state: ViewModelState<List<Product>>,
) {
    item {
        Text(
            text = stringResource(R.string.dashboard_low_stock_title),
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
            val products = state.data
            if (products.isEmpty()) {
                item { DashboardMessageCard(text = stringResource(R.string.dashboard_low_stock_empty)) }
            } else {
                items(items = products, key = { it.id ?: it.barcode }) {
                    ProductItem(it) {}
                }
            }
        }
    }
}


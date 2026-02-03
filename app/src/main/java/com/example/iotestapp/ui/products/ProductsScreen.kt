package com.example.iotestapp.ui.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.iotestapp.R
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.ui.common.HorizontalSpacerLarge
import com.example.iotestapp.ui.common.ViewModelState
import com.example.iotestapp.ui.theme.dimen

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    var searchQuery by remember { mutableStateOf("") }
    val productsState by viewModel.productsState.collectAsState()
    val suppliersState by viewModel.suppliersState.collectAsState()
    val saveProductState by viewModel.saveProductState.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var editProduct by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getProducts()
        viewModel.getSuppliers()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimen.productPadding),
            ) {
                SearchBar(
                    searchQuery = searchQuery,
                    onSearchChange = {
                        searchQuery = it
                        viewModel.updateSearchQuery(it)
                    },
                )
                HorizontalSpacerLarge()

                when (productsState) {
                    is ViewModelState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is ViewModelState.Result -> {
                        ProductsList(
                            (productsState as ViewModelState.Result<List<Product>>).data,
                            stringResource(R.string.product_empty),
                            { editProduct = it },
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(MaterialTheme.dimen.productFabPadding),
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add product")
            }

            if (saveProductState is ViewModelState.Result) {
                showAddDialog = false
                editProduct = null
                viewModel.resetSaveProductState()
            }

            if ((showAddDialog || editProduct != null) && saveProductState !is ViewModelState.Loading) {
                AddEditProduct(
                    product = editProduct,
                    suppliersList = suppliersState as ViewModelState.Result,
                    saveState = saveProductState,
                    onDismiss = {
                        showAddDialog = false
                        editProduct = null
                        viewModel.resetSaveProductState()
                    },
                    onConfirm = { viewModel.saveProduct(it) },
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(id = R.string.product_search_hint)) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchChange("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
    )
}


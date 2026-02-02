package com.example.iotestapp.ui.suppliers

import android.util.Log
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
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.ui.common.HorizontalSpacerLarge
import com.example.iotestapp.ui.common.ViewModelState
import com.example.iotestapp.ui.theme.dimen

@Composable
fun SuppliersScreen(
    viewModel: SuppliersViewModel = hiltViewModel()
) {
    Log.d("SuppliersScreen", "SuppliersScreen: ")
    var searchQuery by remember { mutableStateOf("") }
    val suppliersList by viewModel.suppliersList.collectAsState()
    val saveSupplier by viewModel.saveSupplier.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var editSupplier by remember { mutableStateOf<Supplier?>(null) }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimen.supplierPadding)
            ) {
                SearchBar(
                    searchQuery = searchQuery,
                    onSearchChange = {
                        searchQuery = it
                        viewModel.updateSearchQuery(it)
                    }
                )
                HorizontalSpacerLarge()
                when (suppliersList) {
                    is ViewModelState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is ViewModelState.Result -> {
                        SuppliersList(
                            (suppliersList as ViewModelState.Result<List<Supplier>>).data,
                            stringResource(R.string.supplier_empty),
                            { editSupplier = it }
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(MaterialTheme.dimen.supplierFabPadding)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add supplier")
            }

            if (saveSupplier is ViewModelState.Result) {
                showAddDialog = false
                editSupplier = null
                viewModel.resetSaveSupplierState()
            }

            if (showAddDialog || editSupplier != null) {
                AddEditSupplier(
                    supplier = editSupplier,
                    saveState = saveSupplier,
                    onDismiss = {
                        showAddDialog = false
                        editSupplier = null
                        viewModel.resetSaveSupplierState()
                    },
                    onConfirm = {
                        viewModel.saveSupplier(it)
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchBar(searchQuery: String, onSearchChange: (String) -> Unit) {
    Log.d("SuppliersScreen", "SearchBar: ")
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(id = R.string.supplier_search_hint)) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchChange("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear")
                }
            }
        },
        singleLine = true
    )
}

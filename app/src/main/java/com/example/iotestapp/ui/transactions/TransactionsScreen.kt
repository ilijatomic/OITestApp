@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.iotestapp.ui.transactions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.iotestapp.R
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.ui.common.HorizontalSpacerLarge
import com.example.iotestapp.ui.common.HorizontalSpacerMedium
import com.example.iotestapp.ui.common.HorizontalSpacerSmall
import com.example.iotestapp.ui.common.VerticalSpacerMedium
import com.example.iotestapp.ui.common.ViewModelState
import com.example.testapp.domain.model.Transaction
import com.example.testapp.domain.model.TransactionType

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel = hiltViewModel(),
) {
    val transactionsList by viewModel.transactionsList.collectAsState()
    val productsList by viewModel.productsList.collectAsState()
    val saveState by viewModel.saveTransactionState.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    var selectedType: TransactionType? by remember { mutableStateOf(null) }
    var selectedProduct: Product? by remember { mutableStateOf(null) }

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
                    .padding(16.dp),
            ) {
                TransactionFilterSection(
                    productsList = productsList,
                    selectedType = selectedType,
                    onSelectedTypeChange = { selectedType = it },
                    selectedProduct = selectedProduct,
                    onSelectedProductChange = { selectedProduct = it },
                )

                HorizontalSpacerLarge()

                when (transactionsList) {
                    is ViewModelState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is ViewModelState.Result -> {
                        val filtered = (transactionsList as ViewModelState.Result<List<Transaction>>).data
                            .asSequence()
                            .filter { tx -> selectedType == null || tx.type == selectedType }
                            .filter { tx -> selectedProduct == null || tx.product.id == selectedProduct?.id }
                            .toList()
                        TransactionsList(
                            filtered,
                            stringResource(R.string.transaction_empty)
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(32.dp),
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add transaction")
            }
            if (saveState is ViewModelState.Result) {
                showAddDialog = false
                viewModel.resetSaveTransactionState()
            }

            if (showAddDialog) {
                AddTransaction(
                    productsList = productsList as ViewModelState.Result,
                    saveState = saveState,
                    onDismiss = {
                        showAddDialog = false
                        viewModel.resetSaveTransactionState()
                    },
                    onConfirm = { viewModel.saveTransaction(it) },
                )
            }
        }
    }
}

@Composable
private fun TransactionFilterSection(
    productsList: ViewModelState<List<Product>>,
    selectedType: TransactionType?,
    onSelectedTypeChange: (TransactionType?) -> Unit,
    selectedProduct: Product?,
    onSelectedProductChange: (Product?) -> Unit,
) {
    var filterExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }
    var productExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    true,
                    onClick = { filterExpanded = !filterExpanded }
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Default.FilterList, contentDescription = null)
            VerticalSpacerMedium()
            Text(
                text = stringResource(R.string.transaction_filter),
                style = MaterialTheme.typography.titleSmall,
            )
            Box(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (filterExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
            )
        }

        AnimatedVisibility(visible = filterExpanded) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                val products = (productsList as? ViewModelState.Result<List<Product>>)?.data.orEmpty()

                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = !typeExpanded },
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = true,
                            ),
                        value = selectedType?.name ?: stringResource(R.string.all),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.transaction_filter_type)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.all)) },
                            onClick = {
                                onSelectedTypeChange(null)
                                typeExpanded = false
                            },
                        )
                        TransactionType.entries.forEach {
                            DropdownMenuItem(
                                text = { Text(it.name) },
                                onClick = {
                                    onSelectedTypeChange(it)
                                    typeExpanded = false
                                },
                            )
                        }
                    }
                }

                HorizontalSpacerSmall()

                ExposedDropdownMenuBox(
                    expanded = productExpanded,
                    onExpandedChange = { if (products.isNotEmpty()) productExpanded = !productExpanded },
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = products.isNotEmpty(),
                            ),
                        value = selectedProduct?.name ?: stringResource(R.string.all),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.transaction_filter_product)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = productExpanded) },
                        enabled = products.isNotEmpty(),
                    )
                    ExposedDropdownMenu(
                        expanded = productExpanded,
                        onDismissRequest = { productExpanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.all)) },
                            onClick = {
                                onSelectedProductChange(null)
                                productExpanded = false
                            },
                        )
                        products.forEach {
                            DropdownMenuItem(
                                text = { Text(it.name) },
                                onClick = {
                                    onSelectedProductChange(it)
                                    productExpanded = false
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}


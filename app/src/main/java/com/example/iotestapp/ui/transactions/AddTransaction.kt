package com.example.iotestapp.ui.transactions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.example.iotestapp.R
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.ui.common.HorizontalSpacerSmall
import com.example.iotestapp.ui.common.VerticalSpacerSmall
import com.example.iotestapp.ui.common.ViewModelState
import com.example.iotestapp.ui.theme.dimen
import com.example.testapp.domain.model.Transaction
import com.example.testapp.domain.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransaction(
    productsList: ViewModelState.Result<List<Product>>,
    saveState: ViewModelState<Boolean>,
    onDismiss: () -> Unit,
    onConfirm: (Transaction) -> Unit,
) {

    var productExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }

    var selectedProduct by remember { mutableStateOf(productsList.data.firstOrNull()) }
    var selectedType by remember { mutableStateOf(TransactionType.RESTOCK) }
    var quantityText by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val isSaving = saveState is ViewModelState.Loading

    val inputValid by remember {
        derivedStateOf {
            selectedProduct != null &&
            (quantityText.toIntOrNull() ?: 0) > 0
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(MaterialTheme.dimen.transactionAddDialogShapeSize)) {
            Column(modifier = Modifier.padding(MaterialTheme.dimen.transactionAddDialogPadding)) {
                Text(text = stringResource(R.string.transaction_add))

                ExposedDropdownMenuBox(
                    expanded = productExpanded,
                    onExpandedChange = { productExpanded = !productExpanded },
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = !isSaving && productsList.data.isNotEmpty(),
                        ),
                        value = selectedProduct?.name.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.product_title)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = productExpanded) },
                        enabled = !isSaving && productsList.data.isNotEmpty(),
                    )
                    ExposedDropdownMenu(
                        expanded = productExpanded,
                        onDismissRequest = { productExpanded = false },
                    ) {
                        productsList.data.forEach {
                            DropdownMenuItem(
                                text = { Text(it.name) },
                                onClick = {
                                    selectedProduct = it
                                    productExpanded = false
                                },
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { if (!isSaving) typeExpanded = !typeExpanded },
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = !isSaving,
                        ),
                        value = selectedType.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.transaction_type)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        enabled = !isSaving,
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false },
                    ) {
                        TransactionType.entries.forEach { it
                            DropdownMenuItem(
                                text = { Text(it.name) },
                                onClick = {
                                    selectedType = it
                                    typeExpanded = false
                                },
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { quantityText = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    label = { Text(stringResource(R.string.transaction_quantity)) },
                    singleLine = true,
                    enabled = !isSaving,
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(stringResource(R.string.transaction_notes)) },
                    enabled = !isSaving,
                )

                if (saveState is ViewModelState.Error) {
                    HorizontalSpacerSmall()
                    Text(
                        text = stringResource(saveState.id),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                VerticalSpacerSmall()
                Row(modifier = Modifier.align(Alignment.End)) {
                    Button(onClick = onDismiss, enabled = !isSaving) {
                        Text(stringResource(R.string.cancel))
                    }

                    Button(
                        onClick = {
                            val product = selectedProduct ?: return@Button
                            val qty = quantityText.toIntOrNull() ?: 0
                            onConfirm(
                                Transaction(
                                    id = null,
                                    date = System.currentTimeMillis(),
                                    type = selectedType,
                                    product = product,
                                    quantity = qty,
                                    notes = notes.takeIf { it.isNotBlank() },
                                ),
                            )
                        },
                        enabled = inputValid && !isSaving,
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                        } else {
                            Text(stringResource(R.string.save))
                        }
                    }
                }
            }
        }
    }
}


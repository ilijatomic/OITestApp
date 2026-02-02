package com.example.iotestapp.ui.products

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
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.ui.common.HorizontalSpacerSmall
import com.example.iotestapp.ui.common.VerticalSpacerSmall
import com.example.iotestapp.ui.common.ViewModelState
import com.example.iotestapp.ui.theme.dimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProduct(
    product: Product?,
    suppliersList: ViewModelState.Result<List<Supplier>>,
    saveState: ViewModelState<Boolean>,
    onDismiss: () -> Unit,
    onConfirm: (Product) -> Unit,
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var category by remember { mutableStateOf(product?.category ?: "") }
    var barcode by remember { mutableStateOf(product?.barcode ?: "") }
    var supplier by remember { mutableStateOf(product?.supplier ?: suppliersList.data.firstOrNull()) }
    var currentStockText by remember { mutableStateOf(product?.currentStockLevel?.toString() ?: "") }
    var minimumStockText by remember { mutableStateOf(product?.minimumStockLevel?.toString() ?: "") }

    var supplierExpanded by remember { mutableStateOf(false) }
    val isSaving = saveState is ViewModelState.Loading

    val inputValid by remember {
        derivedStateOf {
            name.isNotBlank() &&
            description.isNotBlank() &&
            category.isNotBlank() &&
            barcode.isNotBlank() &&
            supplier != null &&
            price.toDoubleOrNull() != null &&
            currentStockText.toIntOrNull() != null &&
            minimumStockText.toIntOrNull() != null
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(MaterialTheme.dimen.productAddDialogShapeSize)) {
            Column(modifier = Modifier.padding(MaterialTheme.dimen.productAddDialogPadding)) {
                Text(
                    text = if (product == null) stringResource(R.string.product_add) else stringResource(R.string.product_edit),
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.name)) },
                    singleLine = true,
                    enabled = !isSaving,
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description)) },
                    enabled = !isSaving,
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(stringResource(R.string.price)) },
                    singleLine = true,
                    enabled = !isSaving,
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text(stringResource(R.string.category)) },
                    singleLine = true,
                    enabled = !isSaving,
                )
                OutlinedTextField(
                    value = barcode,
                    onValueChange = { barcode = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(stringResource(R.string.barcode)) },
                    singleLine = true,
                    enabled = !isSaving,
                )

                ExposedDropdownMenuBox(
                    expanded = supplierExpanded,
                    onExpandedChange = { supplierExpanded = !supplierExpanded },
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = !isSaving && suppliersList.data.isNotEmpty(),
                        ),
                        value = supplier?.name.orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.supplier)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = supplierExpanded) },
                        enabled = !isSaving && suppliersList.data.isNotEmpty()
                    )

                    ExposedDropdownMenu(
                        expanded = supplierExpanded,
                        onDismissRequest = { supplierExpanded = false },
                    ) {
                        suppliersList.data.forEach {
                            DropdownMenuItem(
                                text = { Text(it.name) },
                                onClick = {
                                    supplier = it
                                    supplierExpanded = false
                                },
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = currentStockText,
                    onValueChange = { currentStockText = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(stringResource(R.string.current_stock_level)) },
                    singleLine = true,
                    enabled = !isSaving,
                )
                OutlinedTextField(
                    value = minimumStockText,
                    onValueChange = { minimumStockText = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(stringResource(R.string.minimum_stock_level)) },
                    singleLine = true,
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
                            onConfirm(
                                Product(
                                    id = product?.id,
                                    name = name,
                                    description = description,
                                    price = price.toDoubleOrNull() ?: 0.0,
                                    category = category,
                                    barcode = barcode,
                                    supplier = supplier!!,
                                    currentStockLevel = currentStockText.toIntOrNull() ?: 0,
                                    minimumStockLevel = minimumStockText.toIntOrNull() ?: 0,
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


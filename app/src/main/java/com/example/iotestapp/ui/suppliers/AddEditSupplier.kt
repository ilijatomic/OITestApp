package com.example.iotestapp.ui.suppliers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.ui.common.HorizontalSpacerSmall
import com.example.iotestapp.ui.common.ViewModelState
import com.example.iotestapp.ui.theme.dimen

@Composable
fun AddEditSupplier(
    supplier: Supplier?,
    saveState: ViewModelState<Boolean>,
    onDismiss: () -> Unit,
    onConfirm: (Supplier) -> Unit
) {
    var name by remember { mutableStateOf(supplier?.name ?: "") }
    var contactPerson by remember { mutableStateOf(supplier?.contactPerson ?: "") }
    var phone by remember { mutableStateOf(supplier?.phone ?: "") }
    var email by remember { mutableStateOf(supplier?.email ?: "") }
    var address by remember { mutableStateOf(supplier?.address ?: "") }

    val inputValid by remember {
        derivedStateOf {
            name.isNotEmpty() &&
            contactPerson.isNotEmpty() &&
            phone.isNotEmpty() &&
            email.isNotEmpty() &&
            address.isNotEmpty()
        }
    }

    val isSaving = saveState is ViewModelState.Loading

    Dialog(onDismissRequest = onDismiss) {
        println("add supplier")
        
        Surface(shape = RoundedCornerShape(MaterialTheme.dimen.supplierAddDialogShapeSize)) {
            Column(modifier = Modifier.padding(MaterialTheme.dimen.supplierAddDialogPadding)) {
                Text(
                    text = if (supplier == null) stringResource(R.string.supplier_add) else stringResource(R.string.supplier_edit)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.name)) },
                    singleLine = true,
                    enabled = !isSaving
                )
                OutlinedTextField(
                    value = contactPerson,
                    onValueChange = { contactPerson = it },
                    label = { Text(stringResource(R.string.contact_person)) },
                    singleLine = true,
                    enabled = !isSaving
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(stringResource(R.string.phone)) },
                    singleLine = true,
                    enabled = !isSaving
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.email)) },
                    singleLine = true,
                    enabled = !isSaving
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(stringResource(R.string.address)) },
                    singleLine = true,
                    enabled = !isSaving
                )

                if (saveState is ViewModelState.Error) {
                    HorizontalSpacerSmall()
                    Text(
                        text = stringResource(saveState.id),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                HorizontalSpacerSmall()
                Row(modifier = Modifier.align(Alignment.End)) {
                    Button(
                        onClick = onDismiss,
                        enabled = !isSaving
                    ) {
                        Text(stringResource(R.string.cancel))
                    }

                    Button(
                        onClick = {
                            onConfirm(
                                Supplier(
                                    id = supplier?.id,
                                    name = name,
                                    contactPerson = contactPerson,
                                    phone = phone,
                                    email = email,
                                    address = address
                                )
                            )
                        },
                        enabled = inputValid && !isSaving
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

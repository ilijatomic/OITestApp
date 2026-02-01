package com.example.iotestapp.ui.suppliers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.iotestapp.R
import com.example.iotestapp.domain.model.Supplier

@Composable
fun AddEditSupplier(
    supplier: Supplier?,
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
            name.isNotEmpty() && contactPerson.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && address.isNotEmpty()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (supplier == null) stringResource(R.string.supplier_add) else stringResource(R.string.supplier_edit)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.name)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = contactPerson,
                    onValueChange = { contactPerson = it },
                    label = { Text(stringResource(R.string.contact_person)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(stringResource(R.string.phone)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.email)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(stringResource(R.string.address)) },
                    singleLine = true
                )

                Row(modifier = Modifier.align(Alignment.End)) {
                    Button(
                        onClick = onDismiss
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
                        enabled = inputValid
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}

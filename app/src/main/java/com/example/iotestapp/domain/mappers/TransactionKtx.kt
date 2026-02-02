package com.example.iotestapp.domain.mappers

import com.example.iotestapp.data.local.entity.TransactionEntity
import com.example.iotestapp.domain.model.Product
import com.example.testapp.domain.model.Transaction

fun Transaction.toEntity(productId: Long): TransactionEntity = TransactionEntity(
    id = id ?: 0,
    date = date,
    type = type,
    productId = productId,
    quantity = quantity,
    notes = notes,
)

fun TransactionEntity.toDomain(product: Product): Transaction = Transaction(
    id = id,
    date = date,
    type = type,
    product = product,
    quantity = quantity,
    notes = notes,
)


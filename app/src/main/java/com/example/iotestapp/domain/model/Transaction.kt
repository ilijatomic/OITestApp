package com.example.testapp.domain.model

import com.example.iotestapp.domain.model.Product

/**
 * Domain model representing an inventory transaction (restock or sale).
 */
data class Transaction(
    val id: Long,
    /** Epoch milliseconds (e.g. System.currentTimeMillis()). */
    val date: Long,
    val type: TransactionType,
    val product: Product,
    val quantity: Int,
    val notes: String?,
)

/**
 * Type of inventory transaction.
 */
enum class TransactionType {
    RESTOCK,
    SALE
}

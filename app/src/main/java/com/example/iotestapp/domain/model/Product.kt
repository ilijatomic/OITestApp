package com.example.iotestapp.domain.model

/**
 * Domain model representing a product in the inventory.
 */
data class Product(
    val id: Long? = null,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val barcode: String,
    val supplier: Supplier,
    val currentStockLevel: Int,
    val minimumStockLevel: Int,
)

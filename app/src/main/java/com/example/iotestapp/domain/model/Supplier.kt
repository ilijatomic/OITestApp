package com.example.iotestapp.domain.model

/**
 * Domain model representing a product supplier.
 */
data class Supplier(
    val id: Long? = null,
    val name: String,
    val contactPerson: String,
    val phone: String,
    val email: String,
    val address: String,
)

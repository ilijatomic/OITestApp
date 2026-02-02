package com.example.iotestapp.domain.repo

import com.example.iotestapp.domain.model.Supplier

interface SuppliersRepository {
    suspend fun addEditSupplier(supplier: Supplier): Supplier?
    suspend fun getAllSuppliers(): List<Supplier>
}
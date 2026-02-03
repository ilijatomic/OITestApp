package com.example.iotestapp.domain.repo

import com.example.iotestapp.domain.model.Supplier

interface SuppliersRepository {

    /**
     * Adds or edit supplier depending if [Supplier.id] is provided
     * @param supplier provided supplier
     * @return added or edited [Supplier]
     */
    suspend fun addEditSupplier(supplier: Supplier): Supplier?

    /**
     * Gets all suppliers from DB
     * @return list of [Supplier]
     */
    suspend fun getAllSuppliers(): List<Supplier>
}
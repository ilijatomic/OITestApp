package com.example.iotestapp.domain.repo

import com.example.iotestapp.data.local.entity.SupplierEntity
import com.example.iotestapp.data.local.entity.UserEntity
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface SuppliersRepository {
    suspend fun addEditSupplier(supplier: Supplier): SupplierEntity?
    suspend fun getAllSuppliers(): List<SupplierEntity>
}
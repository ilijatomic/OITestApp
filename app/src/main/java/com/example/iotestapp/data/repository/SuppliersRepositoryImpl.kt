package com.example.iotestapp.data.repository

import com.example.iotestapp.data.local.dao.SupplierDao
import com.example.iotestapp.data.local.dao.UserDao
import com.example.iotestapp.data.local.entity.SupplierEntity
import com.example.iotestapp.data.local.entity.UserEntity
import com.example.iotestapp.domain.mappers.toDomain
import com.example.iotestapp.domain.mappers.toEntity
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.domain.model.User
import com.example.iotestapp.domain.repo.LoginRepository
import com.example.iotestapp.domain.repo.SuppliersRepository
import javax.inject.Inject

class SuppliersRepositoryImpl @Inject constructor(
    private val supplierDao: SupplierDao
) : SuppliersRepository {
    override suspend fun addEditSupplier(supplier: Supplier): Supplier? {
        val id = if (supplier.id == null) {
            supplierDao.insertSupplier(supplier.toEntity())
        } else {
            supplierDao.updateSupplier(supplier.toEntity())
            supplier.id
        }
        return supplierDao.getSupplierById(id)?.toDomain()
    }

    override suspend fun getAllSuppliers(): List<Supplier> {
        return supplierDao.getAllSuppliers().map { it.toDomain() }
    }


}
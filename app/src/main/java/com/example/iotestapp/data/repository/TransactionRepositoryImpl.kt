package com.example.iotestapp.data.repository

import com.example.iotestapp.data.local.dao.ProductDao
import com.example.iotestapp.data.local.dao.SupplierDao
import com.example.iotestapp.data.local.dao.TransactionDao
import com.example.iotestapp.domain.mappers.toDomain
import com.example.iotestapp.domain.mappers.toEntity
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.domain.repo.TransactionRepository
import com.example.testapp.domain.model.Transaction
import com.example.testapp.domain.model.TransactionType.RESTOCK
import com.example.testapp.domain.model.TransactionType.SALE
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val productDao: ProductDao,
    private val supplierDao: SupplierDao,
) : TransactionRepository {

    override suspend fun addTransaction(transaction: Transaction): Transaction? {
        val productId = transaction.product.id ?: return null
        val id = transactionDao.insertTransaction(transaction.toEntity(productId))
        when (transaction.type) {
            RESTOCK -> productDao.restock(productId, transaction.quantity)
            SALE -> productDao.sale(productId, transaction.quantity)
        }
        return getTransactionById(id)
    }

    override suspend fun getTransactionById(id: Long): Transaction? {
        val entity = transactionDao.getTransactionById(id) ?: return null
        val productEntity = productDao.getProductById(entity.productId) ?: return null
        val supplierEntity = supplierDao.getSupplierById(productEntity.supplierId) ?: return null
        return entity.toDomain(productEntity.toDomain(supplierEntity.toDomain()))
    }

    override suspend fun getTransactionsByProductId(productId: Long): List<Transaction> {
        val productEntity = productDao.getProductById(productId) ?: return emptyList()
        val supplierEntity = supplierDao.getSupplierById(productEntity.supplierId) ?: return emptyList()
        return transactionDao.getTransactionsByProductId(productId).map {
            it.toDomain(productEntity.toDomain(supplierEntity.toDomain()))
        }
    }

    override suspend fun getAllTransactions(): List<Transaction> {
        val productCache = mutableMapOf<Long, Product?>()
        return transactionDao.getAllTransactions().mapNotNull { entity ->
            val product = productCache.getOrPut(entity.productId) {
                productDao.getProductById(entity.productId)?.let {
                    it.toDomain(supplierDao.getSupplierById(it.supplierId)?.toDomain() ?: return@mapNotNull null)
                }
            } ?: return@mapNotNull null
            entity.toDomain(product)
        }
    }

    override suspend fun getRecentTransactions(limit: Int): List<Transaction> {
        val productCache = mutableMapOf<Long, Product?>()
        return transactionDao.getRecentTransactions(limit).mapNotNull { entity ->
            val product = productCache.getOrPut(entity.productId) {
                productDao.getProductById(entity.productId)?.let {
                    it.toDomain(supplierDao.getSupplierById(it.supplierId)?.toDomain() ?: return@mapNotNull null)
                }
            } ?: return@mapNotNull null
            entity.toDomain(product)
        }
    }
}


package com.example.iotestapp.data.repository

import com.example.iotestapp.data.local.dao.TransactionDao
import com.example.iotestapp.domain.mappers.toDomain
import com.example.iotestapp.domain.mappers.toEntity
import com.example.iotestapp.domain.repo.ProductRepository
import com.example.iotestapp.domain.repo.TransactionRepository
import com.example.testapp.domain.model.Transaction
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val productRepository: ProductRepository,
) : TransactionRepository {

    override suspend fun addTransaction(transaction: Transaction): Transaction? {
        val productId = transaction.product.id ?: return null
        val id = transactionDao.insertTransaction(transaction.toEntity(productId))
        return getTransactionById(id)
    }

    override suspend fun getTransactionById(id: Long): Transaction? {
        val entity = transactionDao.getTransactionById(id) ?: return null
        val product = productRepository.getProductById(entity.productId) ?: return null
        return entity.toDomain(product)
    }

    override suspend fun getTransactionsByProductId(productId: Long): List<Transaction> {
        val product = productRepository.getProductById(productId) ?: return emptyList()
        return transactionDao.getTransactionsByProductId(productId).map { it.toDomain(product) }
    }

    override suspend fun getAllTransactions(): List<Transaction> {
        val productCache = mutableMapOf<Long, com.example.iotestapp.domain.model.Product?>()
        return transactionDao.getAllTransactions().mapNotNull { entity ->
            val product = productCache.getOrPut(entity.productId) {
                productRepository.getProductById(entity.productId)
            } ?: return@mapNotNull null
            entity.toDomain(product)
        }
    }

    override suspend fun getRecentTransactions(limit: Int): List<Transaction> {
        val productCache = mutableMapOf<Long, com.example.iotestapp.domain.model.Product?>()
        return transactionDao.getRecentTransactions(limit).mapNotNull { entity ->
            val product = productCache.getOrPut(entity.productId) {
                productRepository.getProductById(entity.productId)
            } ?: return@mapNotNull null
            entity.toDomain(product)
        }
    }
}


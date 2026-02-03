package com.example.iotestapp.domain.repo

import com.example.iotestapp.domain.model.Product
import com.example.testapp.domain.model.Transaction

interface TransactionRepository {

    /**
     * Adds transaction record to DB
     * @param transaction provided transaction
     * @return added [Transaction]
     */
    suspend fun addTransaction(transaction: Transaction): Transaction?

    /**
     * Gets transaction by [Transaction.id]
     * @param id provided transaction id
     * @return found [Transaction] or null
     */
    suspend fun getTransactionById(id: Long): Transaction?

    /**
     * Gets transactions by [Product.id]
     * @param productId provided product id
     * @return list of [Transaction] that are for specific [Product]
     */
    suspend fun getTransactionsByProductId(productId: Long): List<Transaction>

    /**
     * Gets all transactions from DB
     * @return list of [Transaction]
     */
    suspend fun getAllTransactions(): List<Transaction>

    /**
     * Gets latest transactions with provided limit
     * @param limit of result size
     * @return list of [Transaction] with [limit] size
     */
    suspend fun getRecentTransactions(limit: Int): List<Transaction>
}

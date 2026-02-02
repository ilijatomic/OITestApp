package com.example.iotestapp.domain.repo

import com.example.testapp.domain.model.Transaction

interface TransactionRepository {
    suspend fun addTransaction(transaction: Transaction): Transaction?
    suspend fun getTransactionById(id: Long): Transaction?
    suspend fun getTransactionsByProductId(productId: Long): List<Transaction>
    suspend fun getAllTransactions(): List<Transaction>
    suspend fun getRecentTransactions(limit: Int): List<Transaction>
}


package com.example.iotestapp.domain.usecase.transactions

import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.repo.TransactionRepository
import com.example.testapp.domain.model.Transaction
import javax.inject.Inject

/**
 * Use case for getting list of transactions from DB
 */
class GetAllTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
) {
    suspend operator fun invoke(): Resource<List<Transaction>> {
        return try {
            Resource.Success(transactionRepository.getAllTransactions())
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}


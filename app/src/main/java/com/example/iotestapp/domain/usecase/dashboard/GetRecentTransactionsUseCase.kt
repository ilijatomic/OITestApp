package com.example.iotestapp.domain.usecase.dashboard

import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.repo.TransactionRepository
import com.example.testapp.domain.model.Transaction
import javax.inject.Inject

/**
 * Use case for getting list of recent transactions
 * Default limit is 5
 */
class GetRecentTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
) {
    suspend operator fun invoke(limit: Int = 5): Resource<List<Transaction>> {
        return try {
            Resource.Success(transactionRepository.getRecentTransactions(limit))
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}


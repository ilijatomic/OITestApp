package com.example.iotestapp.domain.usecase.dashboard

import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.repo.TransactionRepository
import com.example.testapp.domain.model.Transaction
import javax.inject.Inject

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


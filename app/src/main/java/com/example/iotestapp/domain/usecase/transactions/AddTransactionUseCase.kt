package com.example.iotestapp.domain.usecase.transactions

import com.example.iotestapp.R
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.repo.TransactionRepository
import com.example.testapp.domain.model.Transaction
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
) {
    suspend operator fun invoke(transaction: Transaction): Resource<Transaction> {
        return try {
            val hasInvalidInput =
                (transaction.product.id == null || transaction.product.id <= 0) ||
                transaction.quantity <= 0 ||
                transaction.date <= 0

            if (hasInvalidInput) {
                return Resource.Error(R.string.invalid_input)
            }

            val result = transactionRepository.addTransaction(transaction)
            result?.let { return Resource.Success(it) }
            Resource.Error(R.string.transaction_error_save)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}


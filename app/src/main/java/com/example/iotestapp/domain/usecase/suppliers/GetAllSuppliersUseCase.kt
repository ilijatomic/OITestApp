package com.example.iotestapp.domain.usecase.suppliers

import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.domain.repo.SuppliersRepository
import javax.inject.Inject

/**
 * Use case for getting list of suppliers from DB
 */
class GetAllSuppliersUseCase @Inject constructor(
    private val suppliersRepository: SuppliersRepository
) {
    suspend operator fun invoke(): Resource<List<Supplier>> {
        return try {
            return Resource.Success(suppliersRepository.getAllSuppliers())
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}
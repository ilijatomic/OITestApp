package com.example.iotestapp.domain.usecase.suppliers

import com.example.iotestapp.R
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.mappers.toDomain
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.domain.repo.SuppliersRepository
import javax.inject.Inject

class GetAllSuppliersUseCase @Inject constructor(
    private val suppliersRepository: SuppliersRepository
) {
    suspend operator fun invoke() : Resource<List<Supplier>> {
        return try {
            return Resource.Success(suppliersRepository.getAllSuppliers().map { it.toDomain() })
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}
package com.example.iotestapp.domain.usecase.dashboard

import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.domain.repo.ProductRepository
import javax.inject.Inject

class GetLowStockProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(): Resource<List<Product>> {
        return try {
            Resource.Success(productRepository.getLowStockProducts())
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}


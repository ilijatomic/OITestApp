package com.example.iotestapp.domain.usecase.products

import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.domain.repo.ProductRepository
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): Resource<List<Product>> {
        return try {
            Resource.Success(productRepository.getAllProducts())
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}


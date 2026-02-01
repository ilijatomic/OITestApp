package com.example.iotestapp.domain.usecase.products

import com.example.iotestapp.R
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.domain.repo.ProductRepository
import javax.inject.Inject

class AddEditProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Resource<Boolean> {
        return try {
            val hasInvalidInput =
                product.name.isBlank() ||
                    product.description.isBlank() ||
                    product.category.isBlank() ||
                    product.barcode.isBlank() ||
                    (product.supplier.id == null || product.supplier.id <= 0)

            if (hasInvalidInput) {
                return Resource.Error(R.string.invalid_input)
            }

            val result = productRepository.addEditProduct(product)
            result?.let { return Resource.Success(true) }
            Resource.Error(R.string.product_error_save)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}


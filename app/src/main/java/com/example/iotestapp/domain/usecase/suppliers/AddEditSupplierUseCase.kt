package com.example.iotestapp.domain.usecase.suppliers

import com.example.iotestapp.R
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.domain.repo.SuppliersRepository
import javax.inject.Inject

class AddEditSupplierUseCase @Inject constructor(
    private val suppliersRepository: SuppliersRepository
) {
    suspend operator fun invoke(supplier: Supplier) : Resource<Boolean> {
        return try {
            val hasInvalidInput =
                supplier.name.isBlank() ||
                        supplier.contactPerson.isBlank() ||
                        supplier.email.isBlank() ||
                        supplier.phone.isBlank() ||
                        supplier.address.isBlank()

            if (hasInvalidInput) {
                return Resource.Error(R.string.invalid_input)
            }

            val result = suppliersRepository.addEditSupplier(supplier)
            result?.let {
                return Resource.Success(true)
            }
            return Resource.Error(R.string.supplier_error_save)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}
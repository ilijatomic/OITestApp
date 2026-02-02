package com.example.iotestapp.domain.repo

import com.example.iotestapp.domain.model.Product

interface ProductRepository {
    suspend fun addEditProduct(product: Product): Product?
    suspend fun getProductById(id: Long): Product?
    suspend fun getProductByBarcode(barcode: String): Product?
    suspend fun getAllProducts(): List<Product>
    suspend fun getLowStockProducts(): List<Product>
}


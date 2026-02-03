package com.example.iotestapp.domain.repo

import com.example.iotestapp.domain.model.Product

interface ProductRepository {

    /**
     * Adds or edit supplier depending if [Product.id] is provided
     * @param product provided product
     * @return added or edited [Product]
     */
    suspend fun addEditProduct(product: Product): Product?

    /**
     * Gets product by [Product.id]
     * @param id provided product id
     * @return found [Product] or null
     */
    suspend fun getProductById(id: Long): Product?

    /**
     * Gets product by [Product.barcode]
     * @param barcode provided product barcode
     * @return found [Product] or null
     */
    suspend fun getProductByBarcode(barcode: String): Product?

    /**
     * Gets all products from DB
     * @return list of [Product]
     */
    suspend fun getAllProducts(): List<Product>

    /**
     * Gets all products that are on low stock
     * @return list of [Product] with low stock
     */
    suspend fun getLowStockProducts(): List<Product>
}


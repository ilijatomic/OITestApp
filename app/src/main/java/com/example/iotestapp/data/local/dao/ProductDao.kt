package com.example.iotestapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.iotestapp.data.local.entity.ProductEntity

@Dao
interface ProductDao {

    @Insert
    suspend fun insertProduct(product: ProductEntity): Long

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): ProductEntity?

    @Query("SELECT * FROM products WHERE barcode = :barcode")
    suspend fun getProductByBarcode(barcode: String): ProductEntity?

    @Query("SELECT * FROM products ORDER BY name ASC")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query(
        """
        SELECT * FROM products
        WHERE currentStockLevel < minimumStockLevel
        ORDER BY (minimumStockLevel - currentStockLevel) DESC
        """
    )
    suspend fun getLowStockProducts(): List<ProductEntity>

    /**
     * Increases [currentStockLevel] by [quantity] for the product with [productId].
     * @return number of rows updated (1 if product exists, 0 otherwise)
     */
    @Query("UPDATE products SET currentStockLevel = currentStockLevel + :quantity WHERE id = :productId")
    suspend fun restock(productId: Long, quantity: Int): Int

    /**
     * Decreases [currentStockLevel] by [quantity] for the product with [productId].
     * Level is clamped to a minimum of 0.
     * @return number of rows updated (1 if product exists, 0 otherwise)
     */
    @Query("UPDATE products SET currentStockLevel = MAX(0, currentStockLevel - :quantity) WHERE id = :productId")
    suspend fun sale(productId: Long, quantity: Int): Int
}


package com.example.iotestapp.data.repository

import com.example.iotestapp.data.local.dao.ProductDao
import com.example.iotestapp.data.local.dao.SupplierDao
import com.example.iotestapp.domain.mappers.toDomain
import com.example.iotestapp.domain.mappers.toEntity
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.domain.repo.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val supplierDao: SupplierDao,
) : ProductRepository {

    override suspend fun addEditProduct(product: Product): Product? {
        val id = if (product.id == null) {
            productDao.insertProduct(product.toEntity())
        } else {
            productDao.updateProduct(product.toEntity())
            product.id
        }

        val entity = productDao.getProductById(id) ?: return null
        val supplier = supplierDao.getSupplierById(entity.supplierId)?.toDomain() ?: return null
        return entity.toDomain(supplier)
    }

    override suspend fun getProductById(id: Long): Product? {
        val entity = productDao.getProductById(id) ?: return null
        val supplier = supplierDao.getSupplierById(entity.supplierId)?.toDomain() ?: return null
        return entity.toDomain(supplier)
    }

    override suspend fun getProductByBarcode(barcode: String): Product? {
        val entity = productDao.getProductByBarcode(barcode) ?: return null
        val supplier = supplierDao.getSupplierById(entity.supplierId)?.toDomain() ?: return null
        return entity.toDomain(supplier)
    }

    override suspend fun getAllProducts(): List<Product> {
        return productDao.getAllProducts().mapNotNull { entity ->
            val supplier = supplierDao.getSupplierById(entity.supplierId)?.toDomain() ?: return@mapNotNull null
            entity.toDomain(supplier)
        }
    }

    override suspend fun getLowStockProducts(): List<Product> {
        val supplierCache = mutableMapOf<Long, com.example.iotestapp.domain.model.Supplier?>()
        return productDao.getLowStockProducts().mapNotNull { entity ->
            val supplier = supplierCache.getOrPut(entity.supplierId) {
                supplierDao.getSupplierById(entity.supplierId)?.toDomain()
            } ?: return@mapNotNull null
            entity.toDomain(supplier)
        }
    }
}


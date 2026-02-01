package com.example.iotestapp.domain.mappers

import com.example.iotestapp.data.local.entity.ProductEntity
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.domain.model.Supplier

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id ?: 0,
    name = name,
    description = description,
    price = price,
    category = category,
    barcode = barcode,
    supplierId = supplier.id!!,
    currentStockLevel = currentStockLevel,
    minimumStockLevel = minimumStockLevel,
)

fun ProductEntity.toDomain(supplier: Supplier): Product = Product(
    id = id,
    name = name,
    description = description,
    price = price,
    category = category,
    barcode = barcode,
    supplier = supplier,
    currentStockLevel = currentStockLevel,
    minimumStockLevel = minimumStockLevel,
)


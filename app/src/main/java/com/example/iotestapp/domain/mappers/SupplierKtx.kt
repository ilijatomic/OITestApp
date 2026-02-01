package com.example.iotestapp.domain.mappers

import com.example.iotestapp.data.local.entity.SupplierEntity
import com.example.iotestapp.data.local.entity.UserEntity
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.domain.model.User
import kotlin.Long

fun Supplier.toEntity() = SupplierEntity(
    id = id ?: 0,
    name = name,
    contactPerson = contactPerson,
    phone = phone,
    email = email,
    address = address
)

fun SupplierEntity.toDomain() = Supplier(
    id = id,
    name = name,
    contactPerson = contactPerson,
    phone = phone,
    email = email,
    address = address
)
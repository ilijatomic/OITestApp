package com.example.iotestapp.domain.mappers

import com.example.iotestapp.data.local.entity.UserEntity
import com.example.iotestapp.domain.model.User

fun User.toEntity() = UserEntity(username = username)
fun UserEntity.toDomain() = User(username = username)
package com.example.iotestapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.iotestapp.data.local.entity.SupplierEntity

@Dao
interface SupplierDao {

    @Insert
    suspend fun insertSupplier(supplier: SupplierEntity): Long

    @Update
    suspend fun updateSupplier(supplier: SupplierEntity)

    @Query("SELECT * FROM suppliers WHERE id = :id")
    suspend fun getSupplierById(id: Long): SupplierEntity?

    @Query("SELECT * FROM suppliers ORDER BY name ASC")
    suspend fun getAllSuppliers(): List<SupplierEntity>
}
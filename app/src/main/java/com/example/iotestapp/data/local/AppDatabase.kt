package com.example.iotestapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.iotestapp.data.local.dao.ProductDao
import com.example.iotestapp.data.local.dao.SupplierDao
import com.example.iotestapp.data.local.dao.TransactionDao
import com.example.iotestapp.data.local.dao.UserDao
import com.example.iotestapp.data.local.entity.ProductEntity
import com.example.iotestapp.data.local.entity.SupplierEntity
import com.example.iotestapp.data.local.entity.TransactionEntity
import com.example.iotestapp.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        SupplierEntity::class,
        ProductEntity::class,
        TransactionEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "app_database"
    }

    abstract fun userDao() : UserDao
    abstract fun supplierDao() : SupplierDao
    abstract fun productDao(): ProductDao
    abstract fun transactionDao(): TransactionDao
}
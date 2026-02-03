package com.example.iotestapp.di

import android.content.Context
import androidx.room.Room
import com.example.iotestapp.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class] // Replaces your actual Hilt module
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideInMemoryDb(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase) = db.userDao()

    @Provides
    @Singleton
    fun provideSupplierDao(db: AppDatabase) = db.supplierDao()

    @Provides
    @Singleton
    fun provideProductDao(db: AppDatabase) = db.productDao()

    @Provides
    @Singleton
    fun provideTransactionDao(db: AppDatabase) = db.transactionDao()
}
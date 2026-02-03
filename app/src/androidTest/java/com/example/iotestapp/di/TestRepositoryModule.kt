package com.example.iotestapp.di

import com.example.iotestapp.data.repository.LoginRepositoryImpl
import com.example.iotestapp.data.repository.ProductRepositoryImpl
import com.example.iotestapp.data.repository.SuppliersRepositoryImpl
import com.example.iotestapp.data.repository.TransactionRepositoryImpl
import com.example.iotestapp.domain.repo.LoginRepository
import com.example.iotestapp.domain.repo.ProductRepository
import com.example.iotestapp.domain.repo.SuppliersRepository
import com.example.iotestapp.domain.repo.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class] // Replaces your actual Hilt module
)
abstract class TestRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindSuppliersRepository(
        suppliersRepositoryImpl: SuppliersRepositoryImpl
    ): SuppliersRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository
}
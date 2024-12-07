package com.currency.conversion.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CurrencyDbRepositoryModule {

    @Binds
    @Singleton
    fun bindCurrencyDbRepository(dbRepository: CurrencyDbRepositoryImpl): CurrencyDbRepository
}
package com.currency.conversion.data.repository

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CurrencyRepositoryModule {

    @Binds
    @Singleton
    fun bindCurrencyRepository(currencyRepo: CurrencyApiRepositoryImpl): CurrencyApiRepository

}
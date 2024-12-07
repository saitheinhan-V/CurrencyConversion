package com.currency.conversion.data.repository

import com.currency.conversion.data.database.entity.CurrencyEntity
import com.currency.conversion.data.database.entity.RateEntity
import kotlinx.coroutines.flow.Flow

interface CurrencyDbRepository {

    suspend fun saveCurrencies(currencies: List<CurrencyEntity>)

    suspend fun getCurrencies(): List<CurrencyEntity>

    suspend fun deleteCurrencies()

    suspend fun saveRates(rates: List<RateEntity>)

    suspend fun getRates(source: String): List<RateEntity>

    suspend fun updateRates(amount: Double,source: String,code: String,timestamp: Long)
}
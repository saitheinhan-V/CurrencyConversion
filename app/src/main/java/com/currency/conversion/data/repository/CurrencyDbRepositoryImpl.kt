package com.currency.conversion.data.repository

import com.currency.conversion.data.database.dao.CurrencyDao
import com.currency.conversion.data.database.dao.RateDao
import com.currency.conversion.data.database.entity.CurrencyEntity
import com.currency.conversion.data.database.entity.RateEntity
import com.currency.conversion.data.network.DispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyDbRepositoryImpl @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val rateDao: RateDao,
    @DispatcherModule.IoDispatcher private val io: CoroutineDispatcher
): CurrencyDbRepository {

    override suspend fun saveCurrencies(currencies: List<CurrencyEntity>) {
        withContext(io){
            currencyDao.insertCurrencies(currencies)
        }
    }

    override suspend fun getCurrencies(): List<CurrencyEntity> {
        return withContext(io){
            currencyDao.readCurrencies()
        }
    }

    override suspend fun deleteCurrencies() {
        withContext(io){
            currencyDao.deleteCurrencies()
        }
    }

    override suspend fun saveRates(rates: List<RateEntity>) {
        withContext(io){
            rateDao.insertRates(rates)
        }
    }

    override suspend fun getRates(
        source: String,
    ): List<RateEntity> {
        return withContext(io){
            rateDao.readRates(source)
        }
    }

    override suspend fun updateRates(amount: Double, source: String,code: String, timestamp: Long) {
        withContext(io){
            rateDao.updateRates(amount,source, code, timestamp)
        }
    }

}
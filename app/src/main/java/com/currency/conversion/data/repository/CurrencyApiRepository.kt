package com.currency.conversion.data.repository

import com.currency.conversion.data.network.NetworkResponse
import com.currency.conversion.domain.model.CurrencyListDTO
import com.currency.conversion.domain.model.CurrencyRateDTO
import kotlinx.coroutines.flow.Flow

interface CurrencyApiRepository {

    suspend fun liveCurrencyRate(
        source: String
    ): Flow<NetworkResponse<CurrencyRateDTO>>

    suspend fun currencyList(): Flow<NetworkResponse<CurrencyListDTO>>
}
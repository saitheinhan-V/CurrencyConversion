package com.currency.conversion.data.repository

import com.currency.conversion.data.network.DispatcherModule
import com.currency.conversion.data.network.NetworkResponse
import com.currency.conversion.data.network.SafeApiCall
import com.currency.conversion.data.service.ApiService
import com.currency.conversion.domain.model.CurrencyListDTO
import com.currency.conversion.domain.model.CurrencyRateDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyApiRepositoryImpl @Inject constructor(
    private val api: ApiService,
    @DispatcherModule.IoDispatcher private val io: CoroutineDispatcher
) : CurrencyApiRepository {

    override suspend fun liveCurrencyRate(
        source: String
    ): Flow<NetworkResponse<CurrencyRateDTO>> {
        val response = SafeApiCall { api.getLiveCurrencyRate(source = source) }

        return flow {
            emit(value = response)
        }.flowOn(io)
    }

    override suspend fun currencyList(): Flow<NetworkResponse<CurrencyListDTO>> {
        val response = SafeApiCall {
            api.getCurrencyList()
        }
        return flow {
            emit(value = response)
        }.flowOn(io)
    }

}
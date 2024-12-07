package com.currency.conversion.domain.usecase

import com.currency.conversion.data.network.NetworkResponse
import com.currency.conversion.data.repository.CurrencyApiRepository
import com.currency.conversion.domain.vo.CurrencyRateVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyRates @Inject constructor(
    private val apiRepo: CurrencyApiRepository
) {
    suspend operator fun invoke(
        source: String
    ): Flow<NetworkResponse<CurrencyRateVo>> {
        return apiRepo.liveCurrencyRate(
            source = source
        ).map {
            when(it){
                is NetworkResponse.Fail -> {
                    NetworkResponse.Fail(
                        message = it.message,
                        type = it.type
                    )
                }
                is NetworkResponse.Success -> {
                    NetworkResponse.Success(data = it.data.toVo())
                }
            }
        }
//        return when (
//            val result = apiRepo.liveCurrencyRate(
//                source = source
//            )) {
//            is NetworkResponse.Fail -> NetworkResponse.Fail(
//                message = result.message,
//                type = result.type
//            )
//
//            is NetworkResponse.Success -> {
//                NetworkResponse.Success(data = result.data.toVo())
//            }
//        }
    }
}
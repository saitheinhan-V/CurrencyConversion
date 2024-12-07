package com.currency.conversion.domain.usecase

import com.currency.conversion.data.network.NetworkResponse
import com.currency.conversion.data.repository.CurrencyApiRepository
import com.currency.conversion.domain.model.CurrencyListDTO
import com.currency.conversion.domain.vo.CurrencyListVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyList @Inject constructor(
    private val apiRepo: CurrencyApiRepository
) {
    suspend operator fun invoke(): Flow<NetworkResponse<CurrencyListVo>> {
        return apiRepo.currencyList().map {
            when (it) {
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
//        return when (val result = apiRepo.currencyList()){
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
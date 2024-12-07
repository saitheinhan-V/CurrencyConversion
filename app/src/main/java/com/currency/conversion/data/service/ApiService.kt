package com.currency.conversion.data.service

import com.currency.conversion.domain.model.CurrencyListDTO
import com.currency.conversion.domain.model.CurrencyRateDTO
import com.currency.conversion.presentation.AppConstant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("live")
    suspend fun getLiveCurrencyRate(
        @Query("access_key")
        accessKey: String ?= AppConstant.API_KEY,
        @Query("source")
        source: String ?= AppConstant.DEFAULT_CURRENCY
    ): Response<CurrencyRateDTO>

    @GET("list")
    suspend fun getCurrencyList(
        @Query("access_key")
        accessKey: String ?= AppConstant.API_KEY
    ): Response<CurrencyListDTO>
}
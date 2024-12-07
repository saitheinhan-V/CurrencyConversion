package com.currency.conversion.domain.vo

import com.currency.conversion.domain.model.CurrencyRateDTO
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyRateVo(
    val success: Boolean ?= false,
    val terms: String ?= "",
    val privacy: String ?= "",
    val timestamp: Long ?= 0L,
    val source: String ?= "",
    val quotes: Map<String, Double> ?= null
)


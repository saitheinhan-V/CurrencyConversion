package com.currency.conversion.domain.model

import com.currency.conversion.domain.vo.CurrencyRateVo
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyRateDTO(
    val success: Boolean ?= false,
    val terms: String ?= "",
    val privacy: String ?= "",
    val timestamp: Long ?= 0L,
    val source: String ?= "",
    val quotes: Map<String, Double> ?= null
){
    fun toVo() = CurrencyRateVo(
            success = success,
            terms = terms,
            privacy = privacy,
            timestamp = timestamp,
            source = source,
            quotes = quotes
        )
}

package com.currency.conversion.domain.vo

import com.currency.conversion.domain.model.CurrencyListDTO
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyListVo(
    val success: Boolean ?= false,
    val terms: String ?= "",
    val privacy: String ?= "",
    val currencies: Map<String, String> ?= null
)


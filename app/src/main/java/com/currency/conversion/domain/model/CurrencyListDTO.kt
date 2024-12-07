package com.currency.conversion.domain.model

import com.currency.conversion.domain.vo.CurrencyListVo
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyListDTO(
    val success: Boolean ?= false,
    val terms: String ?= "",
    val privacy: String ?= "",
    val currencies: Map<String, String> ?= null
){
    fun toVo() = CurrencyListVo(
        success = success,
        terms = terms,
        privacy = privacy,
        currencies = currencies
    )
}

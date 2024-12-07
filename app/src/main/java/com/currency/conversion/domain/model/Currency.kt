package com.currency.conversion.domain.model

data class Rate(
    val source: String,
    val key: String,
    val amount: Double,
    val timestamp: Long,
)

data class Currency(
    val key: String,
    val name: String
)



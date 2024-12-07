package com.currency.conversion.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDTO(
    val message: String
)
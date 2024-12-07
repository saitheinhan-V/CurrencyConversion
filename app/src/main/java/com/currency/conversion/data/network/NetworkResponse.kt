package com.currency.conversion.data.network

sealed class NetworkResponse<out T>(
    open val data: T? = null
) {
    data class Success<out T>(override val data: T) : NetworkResponse<T>()
    data class Fail(
        val message: String,
        val type: ErrorType
    ) : NetworkResponse<Nothing>()
}

sealed interface ErrorType {
    data object Dynamic : ErrorType
    data object Connection : ErrorType
    data object Server : ErrorType
}
package com.currency.conversion.data.network

import android.util.Log
import com.currency.conversion.domain.model.ErrorDTO
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

inline fun <reified T> SafeApiCall(
    json: Json = Json { ignoreUnknownKeys = true },
    apiCall: () -> Response<T>
): NetworkResponse<T> {
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            return NetworkResponse.Success(data = body!!)
        }

        val errorCode = response.code()
        if (errorCode >= 500) {
            return NetworkResponse.Fail(
                message = response.message(),
                type = ErrorType.Server
            )
        }
        val errorBody = response.errorBody()
        return if (errorBody != null) {
            val errorResponse = try {
                json.decodeFromString<ErrorDTO>(errorBody.string())
            } catch (e: Exception) {
                null
            }
            if (errorResponse != null) {
                NetworkResponse.Fail(
                    message = errorResponse.message,
                    type = ErrorType.Dynamic
                )
            } else {
                NetworkResponse.Fail(
                    message = response.message(),
                    type = ErrorType.Dynamic
                )
            }
        } else {
            NetworkResponse.Fail(
                message = response.message(),
                type = ErrorType.Dynamic
            )
        }
    } catch (e: Exception) {
        return when (e) {

            is ConnectException -> {
                NetworkResponse.Fail(
                    message = e.localizedMessage ?: "Connection Exception",
                    type = ErrorType.Connection
                )
            }

            is UnknownHostException -> {
                NetworkResponse.Fail(
                    message = e.localizedMessage ?: "Unknown Host Exception",
                    type = ErrorType.Connection
                )
            }

            else -> {
                NetworkResponse.Fail(
                    message = "Safe ApiCall exception",
                    type = ErrorType.Dynamic
                )
            }
        }
    }
}
package com.matijasokol.repo.datasource.network

import arrow.core.Either
import com.matijasokol.core.error.NetworkError
import io.ktor.client.plugins.ResponseException

suspend fun <T> safeNetworkCall(
    apiCall: suspend () -> T,
): Either<NetworkError, T> = Either.catch {
    apiCall()
}.mapLeft { error ->
    when (error) {
        is ResponseException -> NetworkError.BackendError(
            responseCode = error.response.status.value,
            errorMessage = error.message,
        )
        else -> NetworkError.UnknownNetworkError
    }
}

package com.matijasokol.core.error

sealed interface ClientError

sealed interface NetworkError : ClientError {

    data object UnknownNetworkError : NetworkError

    data class BackendError(
        val responseCode: Int,
        val errorMessage: String?,
    ) : NetworkError
}

package com.matijasokol.core.domain

sealed interface Resource<T> {

    data class Error<T>(
        val ex: Exception
    ) : Resource<T>

    data class Success<T>(
        val data: T
    ) : Resource<T>

    data class Loading<T>(
        val isLoading: Boolean
    ) : Resource<T>
}
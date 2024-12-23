package com.matijasokol.repo.detail

import arrow.core.Either
import com.matijasokol.core.dictionary.Dictionary
import com.matijasokol.core.error.NetworkError
import com.matijasokol.repo.domain.model.Repo
import javax.inject.Inject

class RepoDetailsUiMapper @Inject constructor(
    private val dictionary: Dictionary,
) {

    fun toUiState(
        isLoading: Boolean,
        repoOrError: Either<NetworkError, Repo>,
    ) = when (isLoading) {
        true -> RepoDetailState.Loading
        false -> when (repoOrError) {
            is Either.Left -> RepoDetailState.Error(
                dictionary.getString(R.string.repo_detail_message_cache_error),
            )
            is Either.Right -> RepoDetailState.Success(repoOrError.value)
        }
    }
}

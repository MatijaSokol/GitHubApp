package com.matijasokol.repo.detail

import com.matijasokol.repo.domain.model.Repo

sealed interface RepoDetailState {

    data class Success(val repo: Repo) : RepoDetailState
    data class Error(val errorMessage: String) : RepoDetailState
    data object Loading : RepoDetailState
}

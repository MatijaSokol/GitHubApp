package com.matijasokol.repo.detail

import com.matijasokol.repo.domain.model.Repo

data class RepoDetailState(
    val repo: Repo? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)

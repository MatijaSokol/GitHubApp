package com.matijasokol.uirepodetail

import com.matijasokol.repodomain.model.Repo

data class RepoDetailState(
    val repo: Repo? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)

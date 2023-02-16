package com.matijasokol.ui_repodetail

import com.matijasokol.repo_domain.model.Repo

data class RepoDetailState(
    val repo: Repo? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)
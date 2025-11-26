package com.matijasokol.repo.detail

sealed interface RepoDetailAction {

    data class ShowMessage(val message: String) : RepoDetailAction
}

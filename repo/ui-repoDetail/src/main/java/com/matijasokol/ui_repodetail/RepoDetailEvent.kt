package com.matijasokol.ui_repodetail

sealed interface RepoDetailEvent {

    data class GetRepoDetails(
        val repoId: Int
    ) : RepoDetailEvent
}
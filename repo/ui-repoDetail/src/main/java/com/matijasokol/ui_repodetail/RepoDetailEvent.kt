package com.matijasokol.ui_repodetail

sealed interface RepoDetailEvent {

    data class GetRepoFromCache(
        val repoId: Int
    ) : RepoDetailEvent
}
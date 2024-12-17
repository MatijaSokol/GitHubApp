package com.matijasokol.uirepodetail

sealed interface RepoDetailEvent {

    data class GetRepoDetails(
        val repoId: Int,
    ) : RepoDetailEvent
}

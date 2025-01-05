package com.matijasokol.repo.detail

sealed interface RepoDetailEvent {

    data object OpenProfileWebError : RepoDetailEvent

    data object OpenRepoWebError : RepoDetailEvent
}

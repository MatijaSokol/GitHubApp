package com.matijasokol.core.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object RepoList : Destination

    @Serializable
    data class RepoDetail(val repoId: Int) : Destination
}

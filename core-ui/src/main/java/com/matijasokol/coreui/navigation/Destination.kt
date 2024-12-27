package com.matijasokol.coreui.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object RepoList : Destination

    @Serializable
    data class RepoDetail(
        val repoFullName: String,
        val authorImageUrl: String,
    ) : Destination
}

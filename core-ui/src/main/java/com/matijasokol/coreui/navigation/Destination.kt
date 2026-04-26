package com.matijasokol.coreui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Destination : NavKey {

    @Serializable
    data object RepoList : Destination

    @Serializable
    data class RepoDetail(
        val repoFullName: String,
        val authorImageUrl: String,
    ) : Destination
}

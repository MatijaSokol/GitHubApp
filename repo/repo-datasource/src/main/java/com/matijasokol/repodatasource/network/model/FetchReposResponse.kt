package com.matijasokol.repodatasource.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchReposResponse(
    @SerialName("items")
    val repos: List<RepoDto>,
)

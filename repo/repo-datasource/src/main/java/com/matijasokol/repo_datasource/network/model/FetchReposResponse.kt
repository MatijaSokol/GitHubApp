package com.matijasokol.repo_datasource.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchReposResponse(
    @SerialName("items")
    val repos: List<RepoDto>
)
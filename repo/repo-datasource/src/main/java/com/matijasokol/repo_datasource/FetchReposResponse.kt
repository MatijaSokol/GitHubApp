package com.matijasokol.repo_datasource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchReposResponse(
    @SerialName("items")
    val repos: List<RepoDto>
)
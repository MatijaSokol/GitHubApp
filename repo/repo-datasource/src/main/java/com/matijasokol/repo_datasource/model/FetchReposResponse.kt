package com.matijasokol.repo_datasource.model

import com.matijasokol.repo_datasource.model.RepoDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchReposResponse(
    @SerialName("items")
    val repos: List<RepoDto>
)
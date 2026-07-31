package com.matijasokol.repo.datasource.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchReposResponseDto(
    @SerialName("items")
    val repos: List<RepoDto>,
)

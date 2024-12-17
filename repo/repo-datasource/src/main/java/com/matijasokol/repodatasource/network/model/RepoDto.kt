package com.matijasokol.repodatasource.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("owner")
    val author: AuthorDto,

    @SerialName("forks")
    val forks: Int,

    @SerialName("watchers")
    val watchers: Int,

    @SerialName("open_issues")
    val openIssues: Int,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("stargazers_count")
    val stars: Int,

    @SerialName("topics")
    val topics: List<String>,

    @SerialName("language")
    val language: String?,

    @SerialName("html_url")
    val url: String,

    @SerialName("description")
    val description: String?,
)

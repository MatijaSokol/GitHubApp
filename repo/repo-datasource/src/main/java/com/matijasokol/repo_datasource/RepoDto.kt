package com.matijasokol.repo_datasource

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
    val openIssues: Int
)




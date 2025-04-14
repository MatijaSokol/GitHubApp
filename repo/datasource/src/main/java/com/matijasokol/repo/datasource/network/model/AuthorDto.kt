package com.matijasokol.repo.datasource.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    @SerialName("id")
    val id: Int,
    @SerialName("avatar_url")
    val imageUrl: String,
    @SerialName("login")
    val name: String,
    @SerialName("html_url")
    val profileUrl: String,
    @SerialName("followers_url")
    val followersUrl: String,
    @SerialName("repos_url")
    val reposUrl: String,
)

package com.matijasokol.repo_datasource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    @SerialName("id")
    val id: Int,

    @SerialName("avatar_url")
    val imageUrl: String,

    @SerialName("login")
    val name: String
)
package com.matijasokol.repo.domain.model

data class Author(
    val id: Int,
    val name: String,
    val image: String,
    val profileUrl: String,
    val followersUrl: String,
    val reposUrl: String,
    val followersCount: Int? = null,
    val reposCount: Int? = null,
)

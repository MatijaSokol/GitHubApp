package com.matijasokol.repo.domain.model

import java.util.Date

data class Repo(
    val id: Int,
    val name: String,
    val author: Author,
    val watchersCount: Int,
    val forksCount: Int,
    val issuesCount: Int,
    val lastUpdated: Date,
    val starsCount: Int,
    val topics: List<String>,
    val language: String?,
    val url: String,
    val description: String?,
)

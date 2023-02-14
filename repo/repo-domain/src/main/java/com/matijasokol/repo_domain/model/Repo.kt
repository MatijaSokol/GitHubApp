package com.matijasokol.repo_domain.model

import java.util.Date

data class Repo(
    val id: Int,
    val name: String,
    val author: Author,
    val watchersCount: Int,
    val forksCount: Int,
    val issuesCount: Int,
    val lastUpdated: Date,
    val starsCount: Int
)
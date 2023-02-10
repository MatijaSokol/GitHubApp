package com.matijasokol.repo_domain

data class Repo(
    val id: Int,
    val name: String,
    val author: Author,
    val watchersCount: Int,
    val forksCount: Int,
    val issuesCount: Int
)
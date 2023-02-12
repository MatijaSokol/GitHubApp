package com.matijasokol.repo_domain.model

import com.matijasokol.repo_domain.model.Author

data class Repo(
    val id: Int,
    val name: String,
    val author: Author,
    val watchersCount: Int,
    val forksCount: Int,
    val issuesCount: Int
)
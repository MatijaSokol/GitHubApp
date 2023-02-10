package com.matijasokol.repo_domain

interface RepoService {

    suspend fun fetchRepos(query: String): List<Repo>
}
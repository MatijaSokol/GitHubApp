package com.matijasokol.repo_domain

import com.matijasokol.repo_domain.model.Repo

interface RepoService {

    suspend fun fetchRepos(query: String): List<Repo>
}
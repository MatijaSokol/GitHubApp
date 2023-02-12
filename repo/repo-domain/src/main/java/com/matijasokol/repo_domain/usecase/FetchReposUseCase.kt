package com.matijasokol.repo_domain.usecase

import com.matijasokol.repo_domain.RepoService
import com.matijasokol.repo_domain.model.Repo

class FetchReposUseCase(
    private val repoService: RepoService
) {

    suspend fun execute(query: String, perPage: Int, page: Int): List<Repo> {
        return repoService.fetchRepos(query = query, perPage = perPage, page = page)
    }
}
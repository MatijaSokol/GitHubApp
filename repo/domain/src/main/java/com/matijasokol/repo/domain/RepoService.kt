package com.matijasokol.repo.domain

import com.matijasokol.repo.domain.model.Author
import com.matijasokol.repo.domain.model.Repo

interface RepoService {

    suspend fun fetchRepos(query: String, perPage: Int, page: Int): List<Repo>

    suspend fun fetchAuthorFollowers(followersUrl: String): List<Author>

    suspend fun fetchAuthorRepos(reposUrl: String): List<Repo>
}

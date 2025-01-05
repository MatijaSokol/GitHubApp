package com.matijasokol.repo.domain

import arrow.core.Either
import com.matijasokol.core.error.NetworkError
import com.matijasokol.repo.domain.model.Repo

interface RepoService {

    suspend fun fetchRepos(query: String, perPage: Int, page: Int): Either<NetworkError, List<Repo>>

    suspend fun fetchRepoDetails(repoName: String): Either<NetworkError, Repo>
}

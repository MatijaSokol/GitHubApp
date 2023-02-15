package com.matijasokol.repo_domain

import com.matijasokol.repo_domain.model.Repo

interface RepoCache {

    suspend fun insertRepo(repo: Repo)

    suspend fun insertRepos(repos: List<Repo>)

    suspend fun getRepo(repoId: Int): Repo

    suspend fun getAllRepos(): List<Repo>

    suspend fun deleteAll()

    suspend fun replaceRepos(repos: List<Repo>)
}
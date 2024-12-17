package com.matijasokol.repodomain

import com.matijasokol.repodomain.model.Repo

interface RepoCache {

    suspend fun insertRepo(repo: Repo)

    suspend fun insertRepos(repos: List<Repo>)

    suspend fun getRepo(repoId: Int): Repo

    suspend fun getAllRepos(): List<Repo>

    suspend fun deleteAll()

    suspend fun replaceRepos(repos: List<Repo>)

    suspend fun updateFollowersCount(count: Int, authorId: Int)

    suspend fun updateReposCount(count: Int, authorId: Int)
}

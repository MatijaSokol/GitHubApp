package com.matijasokol.repodomain

import com.matijasokol.repodomain.model.Author
import com.matijasokol.repodomain.model.Repo

interface RepoService {

    suspend fun fetchRepos(query: String, perPage: Int, page: Int): List<Repo>

    suspend fun fetchAuthorFollowers(followersUrl: String): List<Author>

    suspend fun fetchAuthorRepos(reposUrl: String): List<Repo>
}

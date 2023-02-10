package com.matijasokol.repo_datasource

import com.matijasokol.repo_domain.Repo
import com.matijasokol.repo_domain.RepoService
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class RepoServiceImpl @Inject constructor(
    private val httpClient: HttpClient
) : RepoService {

    val url = "https://api.github.com/search/repositories?q=kotlin&per_page=10&page=1"

    override suspend fun fetchRepos(query: String): List<Repo> {
        val result = httpClient.get {
            url(this@RepoServiceImpl.url)
            contentType(ContentType.Application.Json)
        }

        return result.body<FetchReposResponse>().toRepos()
    }
}
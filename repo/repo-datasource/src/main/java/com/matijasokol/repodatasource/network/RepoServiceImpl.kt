package com.matijasokol.repodatasource.network

import com.matijasokol.repodatasource.mappers.toAuthor
import com.matijasokol.repodatasource.mappers.toRepo
import com.matijasokol.repodatasource.network.model.AuthorDto
import com.matijasokol.repodatasource.network.model.FetchReposResponse
import com.matijasokol.repodatasource.network.model.RepoDto
import com.matijasokol.repodomain.RepoService
import com.matijasokol.repodomain.model.Author
import com.matijasokol.repodomain.model.Repo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class RepoServiceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : RepoService {

    override suspend fun fetchRepos(query: String, perPage: Int, page: Int): List<Repo> {
        val result = httpClient.get {
            url(NetworkConstants.BASE_URL)
            contentType(ContentType.Application.Json)
            parameter(NetworkConstants.PARAM_QUERY, query)
            parameter(NetworkConstants.PARAM_PER_PAGE, perPage)
            parameter(NetworkConstants.PARAM_PAGE, page)
        }

        return result.body<FetchReposResponse>().repos.map { it.toRepo() }
    }

    override suspend fun fetchAuthorFollowers(followersUrl: String): List<Author> {
        val result = httpClient.get {
            url(followersUrl)
            contentType(ContentType.Application.Json)
        }

        return result.body<List<AuthorDto>>().map { it.toAuthor() }
    }

    override suspend fun fetchAuthorRepos(reposUrl: String): List<Repo> {
        val result = httpClient.get {
            url(reposUrl)
            contentType(ContentType.Application.Json)
        }

        return result.body<List<RepoDto>>().map { it.toRepo() }
    }
}

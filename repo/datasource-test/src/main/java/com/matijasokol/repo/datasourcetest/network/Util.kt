package com.matijasokol.repo.datasourcetest.network

import com.matijasokol.repo.datasource.mappers.toAuthor
import com.matijasokol.repo.datasource.mappers.toRepo
import com.matijasokol.repo.datasource.network.model.AuthorDto
import com.matijasokol.repo.datasource.network.model.FetchReposResponse
import com.matijasokol.repo.datasource.network.model.RepoDto
import com.matijasokol.repo.domain.model.Author
import com.matijasokol.repo.domain.model.Repo
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
}

fun serializeRepoResponseData(jsonData: String): List<Repo> = json.decodeFromString<FetchReposResponse>(
    jsonData,
).repos.map(RepoDto::toRepo)

fun serializeRepoListData(jsonData: String): List<Repo> = json.decodeFromString<List<RepoDto>>(
    jsonData,
).map(RepoDto::toRepo)

fun serializeAuthorListData(jsonData: String): List<Author> = json.decodeFromString<List<AuthorDto>>(
    jsonData,
).map(AuthorDto::toAuthor)

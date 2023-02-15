package com.matijasokol.repo_datasource_test.network

import com.matijasokol.repo_datasource.mappers.toRepo
import com.matijasokol.repo_datasource.network.model.FetchReposResponse
import com.matijasokol.repo_domain.model.Repo
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

val json = Json {
    ignoreUnknownKeys = true
}

fun serializeRepoListData(jsonData: String): List<Repo> {
    return json.decodeFromString<FetchReposResponse>(jsonData).repos.map { it.toRepo() }
}
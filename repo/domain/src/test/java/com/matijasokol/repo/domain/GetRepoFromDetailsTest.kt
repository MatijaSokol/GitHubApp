package com.matijasokol.repo.domain

import com.matijasokol.core.domain.Resource
import com.matijasokol.repo.domain.model.Repo
import com.matijasokol.repo.domain.usecase.GetRepoDetailsUseCase
import com.matijasokol.repodatasourcetest.cache.RepoCacheFake
import com.matijasokol.repodatasourcetest.cache.RepoDatabaseFake
import com.matijasokol.repodatasourcetest.network.RepoServiceFake
import com.matijasokol.repodatasourcetest.network.RepoServiceResponseType
import com.matijasokol.repodatasourcetest.network.serializeAuthorListData
import com.matijasokol.repodatasourcetest.network.serializeRepoListData
import com.matijasokol.repodatasourcetest.network.serializeRepoResponseData
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetRepoFromDetailsTest {

    private lateinit var repoCache: RepoCache
    private lateinit var getRepoDetails: GetRepoDetailsUseCase

    private val repoList =
        serializeRepoResponseData(ClassLoader.getSystemResource("repo_list_valid.json").readText())
    private val jetbrainsFollowers =
        serializeAuthorListData(ClassLoader.getSystemResource("jetbrains_followers.json").readText())
    private val jetbrainsRepos =
        serializeRepoListData(ClassLoader.getSystemResource("jetbrains_repos.json").readText())

    @BeforeEach
    fun setUp() = runBlocking {
        repoCache = RepoCacheFake(
            repoDatabaseFake = RepoDatabaseFake(),
        )

        repoCache.deleteAll()
        repoCache.insertRepos(repoList)

        getRepoDetails = GetRepoDetailsUseCase(
            repoService = RepoServiceFake.build(
                type = RepoServiceResponseType.GoodData,
            ),
            repoCache = repoCache,
        )
    }

    @Test
    fun getRepoDetailsSuccess() = runBlocking {
        repoCache.updateFollowersCount(
            count = jetbrainsFollowers.distinctBy { it.id }.size,
            authorId = 878437,
        )

        repoCache.updateReposCount(
            count = jetbrainsRepos.distinctBy { it.id }.size,
            authorId = 878437,
        )

        val emissions = getRepoDetails.execute(3432266).toList()

        assert(emissions[0] == Resource.Loading<List<Repo>>(isLoading = true))
        assert(emissions[1] is Resource.Success)
        assert(emissions[2] == Resource.Loading<List<Repo>>(isLoading = false))
    }

    @Test
    fun getRepoDetailsError() = runBlocking {
        val emissions = getRepoDetails.execute(-1).toList()

        assert(emissions[0] == Resource.Loading<List<Repo>>(isLoading = true))
        assert(emissions[1] is Resource.Error)
        assert(emissions[2] == Resource.Loading<List<Repo>>(isLoading = false))
    }
}

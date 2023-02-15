package com.matijasokol.repo_domain

import com.matijasokol.core.domain.Resource
import com.matijasokol.repo_datasource_test.network.FakePaginator
import com.matijasokol.repo_datasource_test.network.RepoServiceFake
import com.matijasokol.repo_datasource_test.network.RepoServiceResponseType
import com.matijasokol.repo_domain.model.Repo
import com.matijasokol.repo_domain.usecase.FetchReposUseCase
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FetchReposTest {

    private lateinit var fetchRepos: FetchReposUseCase

    @Test
    fun getReposSuccess() = runBlocking {
        fetchRepos = FetchReposUseCase(
            paginator = FakePaginator(
                repoService = RepoServiceFake.build(
                    type = RepoServiceResponseType.GoodData
                )
            )
        )

        val emissions = fetchRepos.execute("kotlin", false).toList()

        assert(emissions[0] == Resource.Loading<List<Repo>>(isLoading = true))

        assert(emissions[1] is Resource.Success)
        assert((emissions[1] as Resource.Success).data.size == 50)

        assert(emissions[2] == Resource.Loading<List<Repo>>(isLoading = false))
    }

    @Test
    fun getReposEmpty() = runBlocking {
        fetchRepos = FetchReposUseCase(
            paginator = FakePaginator(
                repoService = RepoServiceFake.build(
                    type = RepoServiceResponseType.EmptyList
                )
            )
        )

        val emissions = fetchRepos.execute("kotlin", false).toList()

        assert(emissions[0] == Resource.Loading<List<Repo>>(isLoading = true))

        assert(emissions[1] is Resource.Success)
        assert((emissions[1] as Resource.Success).data.isEmpty())

        assert(emissions[2] == Resource.Loading<List<Repo>>(isLoading = false))
    }

    @Test
    fun getReposMalformed() = runBlocking {
        fetchRepos = FetchReposUseCase(
            paginator = FakePaginator(
                repoService = RepoServiceFake.build(
                    type = RepoServiceResponseType.MalformedData
                )
            )
        )

        val emissions = fetchRepos.execute("kotlin", false).toList()

        assert(emissions[0] == Resource.Loading<List<Repo>>(isLoading = true))

        assert(emissions[1] is Resource.Error)
        assert(((emissions[1] as Resource.Error).ex is ParseException))

        assert(emissions[2] == Resource.Loading<List<Repo>>(isLoading = false))
    }

    @Test
    fun getReposInvalid() = runBlocking {
        fetchRepos = FetchReposUseCase(
            paginator = FakePaginator(
                repoService = RepoServiceFake.build(
                    type = RepoServiceResponseType.Http404
                )
            )
        )

        val emissions = fetchRepos.execute("kotlin", false).toList()

        assert(emissions[0] == Resource.Loading<List<Repo>>(isLoading = true))

        assert(emissions[1] is Resource.Error)
        assert(((emissions[1] as Resource.Error).ex is ParseException))

        assert(emissions[2] == Resource.Loading<List<Repo>>(isLoading = false))
    }
}
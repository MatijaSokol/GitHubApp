package com.matijasokol.repodomain

import com.matijasokol.core.domain.Resource
import com.matijasokol.repodatasourcetest.cache.RepoCacheFake
import com.matijasokol.repodatasourcetest.cache.RepoDatabaseFake
import com.matijasokol.repodatasourcetest.network.FakePaginator
import com.matijasokol.repodatasourcetest.network.RepoServiceFake
import com.matijasokol.repodatasourcetest.network.RepoServiceResponseType
import com.matijasokol.repodomain.model.Repo
import com.matijasokol.repodomain.usecase.FetchReposUseCase
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class FetchReposTest {

    private lateinit var fetchRepos: FetchReposUseCase

    @Test
    fun getReposSuccess() = runBlocking {
        val repoCache = RepoCacheFake(
            RepoDatabaseFake(),
        )
        fetchRepos = FetchReposUseCase(
            paginator = FakePaginator(
                repoService = RepoServiceFake.build(
                    type = RepoServiceResponseType.GoodData,
                ),
                repoCache = repoCache,
            ),
        )

        val emissions = fetchRepos.execute("kotlin", false).toList()

        assert(emissions[0] == Resource.Loading<List<Repo>>(isLoading = true))

        assert(emissions[1] is Resource.Success)
        assert((emissions[1] as Resource.Success).data.size == 50)
        assert((emissions[1] as Resource.Success).data.size == repoCache.getAllRepos().size)
        assert(
            repoCache.getAllRepos().all { cachedRepo -> (emissions[1] as Resource.Success).data.contains(cachedRepo) },
        )

        assert(emissions[2] == Resource.Loading<List<Repo>>(isLoading = false))
    }

    @Test
    fun getReposEmpty() = runBlocking {
        val repoCache = RepoCacheFake(
            RepoDatabaseFake(),
        )
        fetchRepos = FetchReposUseCase(
            paginator = FakePaginator(
                repoService = RepoServiceFake.build(
                    type = RepoServiceResponseType.EmptyList,
                ),
                repoCache = repoCache,
            ),
        )

        val emissions = fetchRepos.execute("kotlin", false).toList()

        assert(emissions[0] == Resource.Loading<List<Repo>>(isLoading = true))

        assert(emissions[1] is Resource.Success)
        assert((emissions[1] as Resource.Success).data.isEmpty())
        assert((emissions[1] as Resource.Success).data.size == repoCache.getAllRepos().size)
        assert(
            repoCache.getAllRepos().all { cachedRepo -> (emissions[1] as Resource.Success).data.contains(cachedRepo) },
        )

        assert(emissions[2] == Resource.Loading<List<Repo>>(isLoading = false))
    }

    @Test
    fun getReposMalformed() = runBlocking {
        val repoCache = RepoCacheFake(
            RepoDatabaseFake(),
        )
        fetchRepos = FetchReposUseCase(
            paginator = FakePaginator(
                repoService = RepoServiceFake.build(
                    type = RepoServiceResponseType.MalformedData,
                ),
                repoCache = repoCache,
            ),
        )

        val emissions = fetchRepos.execute("kotlin", false).toList()

        assert(emissions[0] == Resource.Loading<List<Repo>>(isLoading = true))

        assert(emissions[1] is Resource.Error)
        assert(((emissions[1] as Resource.Error).ex is ParseException))

        assert(emissions[2] == Resource.Loading<List<Repo>>(isLoading = false))
    }

    @Test
    fun getReposInvalid() = runBlocking {
        val repoCache = RepoCacheFake(
            RepoDatabaseFake(),
        )
        fetchRepos = FetchReposUseCase(
            paginator = FakePaginator(
                repoService = RepoServiceFake.build(
                    type = RepoServiceResponseType.Http404,
                ),
                repoCache = repoCache,
            ),
        )

        val emissions = fetchRepos.execute("kotlin", false).toList()

        assert(emissions[0] == Resource.Loading<List<Repo>>(isLoading = true))

        assert(emissions[1] is Resource.Error)
        assert(((emissions[1] as Resource.Error).ex is ParseException))

        assert(emissions[2] == Resource.Loading<List<Repo>>(isLoading = false))
    }
}

package com.matijasokol.repo.datasource.network

import app.cash.turbine.test
import com.matijasokol.repo.datasourcetest.network.RepoServiceFake
import com.matijasokol.repo.datasourcetest.network.RepoServiceResponseType
import com.matijasokol.repo.domain.Paginator
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be`
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.jupiter.api.Test

class BasicPaginatorTest {

    private lateinit var sut: Paginator
    private val query = "kotlin"

    @Test
    fun `should RETURN DATA when request was successful`() = runTest {
        sut = BasicPaginator(
            repoService = RepoServiceFake.build(
                type = RepoServiceResponseType.GoodData,
            ),
        )

        launch {
            sut.loadState.test {
                awaitItem() `should be` Paginator.LoadState.Refresh
                awaitItem() `should be` Paginator.LoadState.Loaded
            }
        }

        launch {
            sut.getData(query).test {
                awaitItem().shouldNotBeEmpty()
            }
        }
    }

    @Test
    fun `should RETURN EMPTY LIST when request fails`() = runTest {
        sut = BasicPaginator(
            repoService = RepoServiceFake.build(
                type = RepoServiceResponseType.MalformedData,
            ),
        )

        launch {
            sut.loadState.test {
                awaitItem() `should be` Paginator.LoadState.Refresh
                awaitItem() `should be` Paginator.LoadState.RefreshError
            }
        }

        launch {
            sut.getData(query).test {
                awaitItem().shouldBeEmpty()
            }
        }
    }

    @Test
    fun `should RETURN APPEND STATUS when new page is requested`() = runTest {
        sut = BasicPaginator(
            repoService = RepoServiceFake.build(
                type = RepoServiceResponseType.GoodData,
            ),
        )

        launch {
            sut.loadState.test {
                awaitItem() `should be` Paginator.LoadState.Refresh
                awaitItem() `should be` Paginator.LoadState.Loaded

                sut.nextPage()

                awaitItem() `should be` Paginator.LoadState.Append
                awaitItem() `should be` Paginator.LoadState.Loaded
            }
        }

        launch {
            sut.getData(query).test {
                awaitItem().shouldNotBeEmpty()
                awaitItem().shouldNotBeEmpty()
            }
        }
    }
}

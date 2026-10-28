package com.matijasokol.repo.list

import app.cash.turbine.test
import com.matijasokol.repo.datasource.network.BasicPaginator
import com.matijasokol.repo.datasourcetest.network.RepoServiceFake
import com.matijasokol.repo.datasourcetest.network.RepoServiceResponseType
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.usecase.SortReposUseCase
import com.matijasokol.test.coroutines.MainDispatcherExtension
import com.matijasokol.test.coroutines.asAppDispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be`
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
class RepoListViewModelTest {

    private lateinit var sut: RepoListViewModel

    @Test
    fun `should RETURN REFRESH STATE when query is set`() = runTest {
        sut = buildSut()

        sut.state.test {
            awaitItem().run {
                loadState `should be` Paginator.LoadState.Refresh
                items.shouldBeEmpty()
                query `should be` DEFAULT_QUERY
            }
        }
    }

    @Test
    fun `should RETURN SUCCESS STATE when request was successful`() = runTest {
        sut = buildSut()

        sut.state.test {
            awaitItem() // initial state

            awaitItem().loadState `should be` Paginator.LoadState.Loaded
            awaitItem().items.shouldNotBeEmpty()
        }
    }

    @Test
    fun `should RETURN EMPTY STATE when request returns no repositories`() = runTest {
        sut = buildSut(RepoServiceResponseType.EmptyList)

        sut.state.test {
            awaitItem() // initial state

            awaitItem().run {
                loadState `should be` Paginator.LoadState.Loaded
                items.shouldBeEmpty()
            }
        }
    }

    @Test
    fun `should KEEP SUCCESS STATE when query is cleared`() = runTest {
        sut = buildSut()

        sut.state.test {
            awaitItem() // initial state
            awaitItem().loadState `should be` Paginator.LoadState.Loaded
            awaitItem().items.shouldNotBeEmpty()

            sut.onEvent(RepoListEvent.OnQueryChanged(""))

            awaitItem().run {
                loadState `should be` Paginator.LoadState.Loaded
                items.shouldNotBeEmpty()
                query `should be` ""
            }
        }
    }

    @Test
    fun `should RETURN ERROR STATE when request fails`() = runTest {
        sut = buildSut(RepoServiceResponseType.Http404)

        sut.state.test {
            awaitItem() // initial state

            // everything is inside this emission since distinctUntilChanged is applied on items
            awaitItem().run {
                loadState `should be` Paginator.LoadState.RefreshError
                items.shouldBeEmpty()
            }
        }
    }

    @Test
    fun `should RETURN APPEND STATE when new page is requested`() = runTest {
        sut = buildSut(RepoServiceResponseType.Http404)

        sut.state.test {
            awaitItem() // initial state

            // everything is inside this emission since distinctUntilChanged is applied on items
            awaitItem().run {
                loadState `should be` Paginator.LoadState.RefreshError
                items.shouldBeEmpty()
            }

            sut.onEvent(RepoListEvent.OnRetryClick)

            awaitItem().loadState `should be` Paginator.LoadState.Refresh
        }
    }

    @Test
    fun `should RETURN REFRESH STATE when retry is clicked after error is received`() = runTest {
        sut = buildSut()

        sut.state.test {
            awaitItem() // initial state

            awaitItem().loadState `should be` Paginator.LoadState.Loaded
            awaitItem().items.shouldNotBeEmpty()

            sut.onEvent(RepoListEvent.LoadMore)

            awaitItem().loadState `should be` Paginator.LoadState.Append
        }
    }

    private fun TestScope.buildSut(
        responseType: RepoServiceResponseType = RepoServiceResponseType.GoodData,
    ): RepoListViewModel {
        val testDispatcher = StandardTestDispatcher(testScheduler)

        return RepoListViewModel(
            sortReposUseCase = SortReposUseCase(appDispatchers = testDispatcher.asAppDispatchers()),
            paginator = BasicPaginator(
                repoService = RepoServiceFake.build(
                    type = responseType,
                    dispatcher = testDispatcher,
                ),
            ),
            repoListUiMapper = RepoListUiMapper(),
        )
    }
}

package com.matijasokol.repo.list

import app.cash.turbine.test
import com.matijasokol.repo.datasourcetest.network.FakePaginator
import com.matijasokol.repo.datasourcetest.network.RepoServiceFake
import com.matijasokol.repo.datasourcetest.network.RepoServiceResponseType
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.usecase.SortReposUseCase
import com.matijasokol.test.FakeDictionary
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be`
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(AndroidCoroutinesExtension::class)
class RepoListViewModelTest {

    private lateinit var sut: RepoListViewModel

    private val sortRepos = SortReposUseCase()
    private val uiMapper = RepoListUiMapper(dictionary = FakeDictionary())

    @Test
    fun `should RETURN REFRESH STATE when query is set`() = runTest {
        sut = RepoListViewModel(
            sortRepos = sortRepos,
            paginator = FakePaginator(
                repoService = RepoServiceFake.build(
                    RepoServiceResponseType.GoodData,
                ),
            ),
            uiMapper = uiMapper,
        )

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
        sut = RepoListViewModel(
            sortRepos = sortRepos,
            paginator = FakePaginator(
                repoService = RepoServiceFake.build(
                    RepoServiceResponseType.GoodData,
                ),
            ),
            uiMapper = uiMapper,
        )

        sut.state.test {
            awaitItem() // initial state
            awaitItem() // sort menu options generated

            awaitItem().loadState `should be` Paginator.LoadState.Loaded
            awaitItem().items.shouldNotBeEmpty()
        }
    }

    @Test
    fun `should RETURN ERROR STATE when request fails`() = runTest {
        sut = RepoListViewModel(
            sortRepos = sortRepos,
            paginator = FakePaginator(
                repoService = RepoServiceFake.build(
                    RepoServiceResponseType.Http404,
                ),
            ),
            uiMapper = uiMapper,
        )

        sut.state.test {
            awaitItem() // initial state
            awaitItem() // sort menu options generated

            // everything is inside this emission since distinctUntilChanged is applied on items
            awaitItem().run {
                loadState `should be` Paginator.LoadState.RefreshError
                items.shouldBeEmpty()
            }
        }
    }

    @Test
    fun `should RETURN APPEND STATE when new page is requested`() = runTest {
        sut = RepoListViewModel(
            sortRepos = sortRepos,
            paginator = FakePaginator(
                repoService = RepoServiceFake.build(
                    RepoServiceResponseType.Http404,
                ),
            ),
            uiMapper = uiMapper,
        )

        sut.state.test {
            awaitItem() // initial state
            awaitItem() // sort menu options generated

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
        sut = RepoListViewModel(
            sortRepos = sortRepos,
            paginator = FakePaginator(
                repoService = RepoServiceFake.build(
                    RepoServiceResponseType.GoodData,
                ),
            ),
            uiMapper = uiMapper,
        )

        sut.state.test {
            awaitItem() // initial state
            awaitItem() // sort menu options generated

            awaitItem().loadState `should be` Paginator.LoadState.Loaded
            awaitItem().items.shouldNotBeEmpty()

            sut.onEvent(RepoListEvent.LoadMore)

            awaitItem().loadState `should be` Paginator.LoadState.Append
        }
    }
}

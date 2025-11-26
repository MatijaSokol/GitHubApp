package com.matijasokol.repo.datasource.network

import arrow.core.Either
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoService
import com.matijasokol.repo.domain.model.Repo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class BasicPaginator @Inject constructor(private val repoService: RepoService) : Paginator {

    override val loadState = MutableStateFlow(Paginator.LoadState.Refresh)
    private val page = MutableStateFlow(INITIAL_PAGE)
    private val allItems = MutableStateFlow(emptyList<Repo>())
    private val retryTrigger = Channel<Unit>()

    private var endReached = false

    override fun getData(query: String): Flow<List<Repo>> {
        page.update { INITIAL_PAGE }
        loadState.update { Paginator.LoadState.Refresh }
        allItems.update { emptyList() }

        return combine(
            page,
            retryTrigger.receiveAsFlow().onStart { emit(Unit) },
        ) { page, _ -> fetchAndProcessData(page, query) }
            .flatMapLatest { newData ->
                allItems.apply { update { (it + newData).distinctBy(Repo::id) } }
            }
    }

    private suspend fun fetchAndProcessData(page: Int, query: String): List<Repo> {
        val newDataResult = repoService.fetchRepos(
            query = query,
            perPage = 30,
            page = page,
        )

        loadState.update {
            when (newDataResult) {
                is Either.Left -> when (page) {
                    INITIAL_PAGE -> Paginator.LoadState.RefreshError
                    else -> Paginator.LoadState.AppendError
                }
                is Either.Right -> Paginator.LoadState.Loaded
            }
        }
        endReached = newDataResult.getOrNull()?.isEmpty() ?: false

        return newDataResult.getOrNull().orEmpty()
    }

    override fun nextPage() {
        if (endReached) return

        loadState.update { Paginator.LoadState.Append }
        page.update { currentPage -> currentPage + 1 }
    }

    override suspend fun retry() {
        loadState.update {
            when (page.value) {
                INITIAL_PAGE -> Paginator.LoadState.Refresh
                else -> Paginator.LoadState.Append
            }
        }

        retryTrigger.send(Unit)
    }
}

private const val INITIAL_PAGE = 1

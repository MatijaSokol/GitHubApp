package com.matijasokol.ui_repolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matijasokol.repo_domain.model.Repo
import com.matijasokol.repo_domain.usecase.FetchReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val fetchRepos: FetchReposUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RepoListState())
    val state = _state.asStateFlow()

    val a = MutableSharedFlow<Int>(replay = 1).apply {
        this.replayCache
    }

    private val _refreshTrigger = MutableStateFlow(Info())
    private val refreshTrigger = _refreshTrigger
        .debounce { (refreshTrigger, _) ->
            when (refreshTrigger) {
                is RefreshTrigger.Query -> 500
                else -> 0
            }
        }
        .distinctUntilChanged { old, new ->
            when (new.refreshTrigger) {
                RefreshTrigger.NextPage -> state.value.endReached
                is RefreshTrigger.Query -> old.query == new.query
                else -> false
            }
        }
        .map { refreshList(it.query, it.refreshTrigger) }
        .onEach { shouldFetchNextPage ->
            if (shouldFetchNextPage) onEvent(RepoListEvent.LoadMore)
        }

    init {
        refreshTrigger.launchIn(viewModelScope)
    }

//    private val paginator = DefaultPaginator(
//        initialKey = state.value.page,
//        onLoadUpdated = { isLoading ->
//            _state.update { it.copy(isLoading = isLoading) }
//        },
//        onRequest = { nextPage ->
//            runCatching {
//                fetchRepos.execute(
//                    query = state.value.query,
//                    perPage = 30,
//                    page = state.value.page
//                )
//            }
//        },
//        getNextKey = { state.value.page + 1 },
//        onError = { ex ->
//            _state.update {
//                it.copy(
//                    error = ex?.localizedMessage
//                )
//            }
//        },
//        onSuccess = { items, newKey ->
//            val containsAll = state.value.items.containsAll(items)
//
//            _state.update {
//                it.copy(
//                    items = (state.value.items + items).distinctBy { repo -> repo.id },
//                    page = newKey,
//                    endReached = items.isEmpty()
//                )
//            }
//
//            if (containsAll) loadNextItems()
//        }
//    )

    fun onEvent(event: RepoListEvent) {
        when (event) {
            RepoListEvent.LoadMore -> viewModelScope.launch {
                _refreshTrigger.update { it.copy(refreshTrigger = RefreshTrigger.NextPage) }
            }
            is RepoListEvent.NavigateToRepoDetails -> {}
            is RepoListEvent.OnQueryChanged -> {
                val updatedState = _state.updateAndGet {
                    it.copy(query = event.query)
                }
                viewModelScope.launch {
                    _refreshTrigger.update { it.copy(
                        refreshTrigger = RefreshTrigger.Query,
                        query = updatedState.query
                    )}
                }
            }
            RepoListEvent.PullToRefreshTriggered -> viewModelScope.launch {
                _refreshTrigger.update { it.copy(refreshTrigger = RefreshTrigger.PullToRefresh) }
            }
        }
    }

    private suspend fun refreshList(query: String, refreshTrigger: RefreshTrigger): Boolean {
        _state.update { it.copy(isLoading = true) }
        val result = fetchData(query, refreshTrigger)
        return updateState(query, refreshTrigger, result)
    }

    private suspend fun fetchData(query: String, refreshTrigger: RefreshTrigger): Result<List<Repo>> {
        val pageToLoad = when (refreshTrigger) {
            RefreshTrigger.NextPage -> state.value.page + 1
            else -> 0
        }

        return runCatching {
            fetchRepos.execute(query, 30, pageToLoad)
        }
    }

    private fun updateState(
        query: String,
        refreshTrigger: RefreshTrigger,
        result: Result<List<Repo>>
    ): Boolean {
        val itemsOrNull = result.getOrNull()
        val containsAll = if (itemsOrNull == null) {
            false
        } else {
            state.value.items.containsAll(itemsOrNull)
        }

        _state.update {
            it.copy(
                items = when (refreshTrigger) {
                    RefreshTrigger.NextPage -> (it.items + itemsOrNull.orEmpty()).distinctBy { it.id }
                    else -> itemsOrNull.orEmpty()
                },
                isLoading = false,
                query = query,
                page = when (refreshTrigger) {
                    RefreshTrigger.NextPage -> state.value.page + 1
                    else -> 0
                },
                endReached = when (refreshTrigger) {
                    RefreshTrigger.NextPage -> itemsOrNull == null
                    else -> state.value.endReached
                }
            )
        }

        return containsAll
    }

    suspend fun updateInfo(refreshTrigger: RefreshTrigger?, query: String?) {
        val current = _refreshTrigger.replayCache.firstOrNull() ?: return
        _refreshTrigger.emit(
            current.copy(
                refreshTrigger = refreshTrigger ?: current.refreshTrigger,
                query = query ?: current.query
            )
        )
    }
}

class Info(
    val refreshTrigger: RefreshTrigger = RefreshTrigger.Initial,
    val query: String = "kotlin"
) {

    fun copy(
        refreshTrigger: RefreshTrigger = this.refreshTrigger,
        query: String = this.query
    ): Info {
        return Info(
            refreshTrigger = refreshTrigger,
            query = query
        )
    }

    operator fun component1() = this.refreshTrigger

    operator fun component2() = this.query
}
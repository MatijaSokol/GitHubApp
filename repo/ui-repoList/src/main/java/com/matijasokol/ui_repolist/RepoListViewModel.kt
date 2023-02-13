package com.matijasokol.ui_repolist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matijasokol.repo_domain.model.Repo
import com.matijasokol.repo_domain.usecase.FetchReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val fetchRepos: FetchReposUseCase,
    private val context: Application
) : ViewModel() {

    private val defaultQuery = "kotlin"

    private val _state = MutableStateFlow(RepoListState().copy(query = defaultQuery))
    val state = _state.asStateFlow()

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
        .mapLatest { refreshList(it.query, it.refreshTrigger) }
        .onEach { shouldFetchNextPage ->
            if (shouldFetchNextPage) onEvent(RepoListEvent.LoadMore)
        }

    init {
        refreshTrigger.launchIn(viewModelScope)
    }

    fun onEvent(event: RepoListEvent) {
        when (event) {
            RepoListEvent.LoadMore -> viewModelScope.launch {
                _refreshTrigger.update { it.copy(refreshTrigger = RefreshTrigger.NextPage) }
            }
            is RepoListEvent.OnQueryChanged -> {
                val updatedState = _state.updateAndGet {
                    it.copy(
                        query = event.query,
                        removeQueryEnabled = event.query.isNotEmpty()
                    )
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

        return fetchRepos.execute(query, 30, pageToLoad)
    }

    private fun updateState(
        query: String,
        refreshTrigger: RefreshTrigger,
        result: Result<List<Repo>>
    ): Boolean {
        val itemsOrNull = result.getOrNull()

        val shouldFetchNextPage = when {
            itemsOrNull == null -> false
            state.value.items.containsAll(itemsOrNull)
                    && refreshTrigger is RefreshTrigger.NextPage -> true
            else -> false
        }

        val allItems = when (refreshTrigger) {
            RefreshTrigger.NextPage -> (state.value.items + itemsOrNull.orEmpty()).distinctBy { it.id }
            else -> itemsOrNull.orEmpty()
        }

        _state.update {
            it.copy(
                items = allItems,
                isLoading = false,
                query = query,
                page = when (refreshTrigger) {
                    RefreshTrigger.NextPage -> state.value.page + 1
                    else -> 0
                },
                endReached = when (refreshTrigger) {
                    RefreshTrigger.NextPage -> itemsOrNull == null
                    else -> state.value.endReached
                },
                scrollToTop = when (refreshTrigger) {
                    RefreshTrigger.Query -> true
                    else -> false
                },
                removeQueryEnabled = query.isNotEmpty(),
                infoMessage = when {
                    allItems.isEmpty() && itemsOrNull == null -> context.getString(R.string.repo_list_message_error)
                    allItems.isEmpty() -> context.getString(R.string.repo_list_message_empty)
                    else -> ""
                }
            )
        }

        return shouldFetchNextPage
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
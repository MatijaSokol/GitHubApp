package com.matijasokol.repo.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matijasokol.core.domain.Resource
import com.matijasokol.repodomain.NetworkException
import com.matijasokol.repodomain.ParseException
import com.matijasokol.repodomain.model.Repo
import com.matijasokol.repodomain.usecase.FetchReposUseCase
import com.matijasokol.repodomain.usecase.SortReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject

const val DEFAULT_QUERY = "kotlin"

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val fetchRepos: FetchReposUseCase,
    private val sortRepos: SortReposUseCase,
    private val context: Application,
) : ViewModel() {

    private val _actions = Channel<RepoListAction>(capacity = BUFFERED)
    val actions = _actions.receiveAsFlow()

    private val _state = MutableStateFlow(RepoListState().copy(query = DEFAULT_QUERY))
    val state = _state.asStateFlow()

    private val _refreshTrigger = MutableStateFlow(RefreshTriggerInfo())
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
        .filter {
            when (it.refreshTrigger) {
                RefreshTrigger.NextPage -> !state.value.isLoading
                else -> true
            }
        }
        .flatMapLatest { info ->
            fetchRepos.execute(
                query = info.query,
                shouldReset = listOf(RefreshTrigger.PullToRefresh, RefreshTrigger.Query).contains(info.refreshTrigger),
            ).map { repos -> updateState(info, repos) }
                .onEach { shouldFetchNewPage -> if (shouldFetchNewPage) onEvent(RepoListEvent.LoadMore) }
        }
        .flowOn(Dispatchers.IO)

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
                        removeQueryEnabled = event.query.isNotEmpty(),
                    )
                }
                viewModelScope.launch {
                    _refreshTrigger.update {
                        it.copy(
                            refreshTrigger = RefreshTrigger.Query,
                            query = updatedState.query,
                        )
                    }
                }
            }
            RepoListEvent.PullToRefreshTriggered -> viewModelScope.launch {
                _refreshTrigger.update { it.copy(refreshTrigger = RefreshTrigger.PullToRefresh) }
            }
            is RepoListEvent.UpdateSortType -> viewModelScope.launch {
                _state.update {
                    it.copy(
                        repoSortType = event.repoSortType,
                        items = sortRepos.execute(it.items, event.repoSortType),
                        scrollToTop = true,
                    )
                }
            }
            RepoListEvent.ScrollToTopExecuted -> _state.update {
                it.copy(scrollToTop = false)
            }
            RepoListEvent.SortMenuOptionsDismissed -> _state.update {
                it.copy(sortMenuVisible = false)
            }
            RepoListEvent.ToggleSortMenuOptionsVisibility -> _state.update {
                it.copy(sortMenuVisible = !it.sortMenuVisible)
            }
            RepoListEvent.UIMessageShown -> _state.update {
                it.copy(uiMessages = it.uiMessages.drop(1))
            }
            is RepoListEvent.OnItemClick -> viewModelScope.launch {
                _actions.send(RepoListAction.NavigateToDetails(event.repoId))
            }
            is RepoListEvent.OnImageClick -> viewModelScope.launch {
                _actions.send(RepoListAction.OpenProfile(event.profileUrl))
            }
        }
    }

    private fun updateState(
        info: RefreshTriggerInfo,
        resource: Resource<List<Repo>>,
    ): Boolean {
        // Sometimes new page returns data which is already contained in previous page.
        // In that case, automatically start fetching a new page.
        val forceFetchNextPage = (resource as? Resource.Success)?.data
            ?.takeIf { it.isNotEmpty() && info.refreshTrigger is RefreshTrigger.NextPage }
            ?.let { state.value.items.containsAll(it) }
            ?: false

        when (resource) {
            is Resource.Error -> _state.update {
                when (resource.ex) {
                    is ParseException, is NetworkException -> it.copy(
                        infoMessage = when (info.refreshTrigger) {
                            RefreshTrigger.NextPage -> ""
                            RefreshTrigger.PullToRefresh ->
                                if (it.items.isEmpty()) getMessageForError(resource.ex) else ""
                            else -> getMessageForError(resource.ex)
                        },
                        uiMessages = when (info.refreshTrigger) {
                            RefreshTrigger.NextPage, RefreshTrigger.PullToRefresh ->
                                it.uiMessages + getMessageForError(resource.ex)
                            else -> it.uiMessages
                        },
                        items = when (info.refreshTrigger) {
                            RefreshTrigger.NextPage -> state.value.items
                            else -> emptyList()
                        },
                    )
                    else -> it
                }
            }
            is Resource.Loading -> _state.update {
                it.copy(
                    isLoading = resource.isLoading,
                    infoMessage = if (resource.isLoading) "" else it.infoMessage,
                )
            }
            is Resource.Success -> _state.update {
                it.copy(
                    items = when (info.refreshTrigger) {
                        RefreshTrigger.NextPage -> (state.value.items + resource.data).distinctBy { repo -> repo.id }
                        else -> resource.data
                    },
                    endReached = resource.data.isEmpty(),
                    infoMessage = "",
                    scrollToTop = when (info.refreshTrigger) {
                        RefreshTrigger.Query -> true
                        else -> state.value.scrollToTop
                    },
                )
            }
        }

        return forceFetchNextPage
    }

    fun getMessageForError(exception: Exception): String {
        return when (exception) {
            is ParseException -> context.getString(R.string.repo_list_message_error)
            is NetworkException -> context.getString(R.string.repo_list_message_network_error)
            else -> ""
        }
    }
}

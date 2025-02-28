package com.matijasokol.repo.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matijasokol.coreui.viewmodel.stateIn
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.domain.usecase.SortReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    sortRepos: SortReposUseCase,
    uiMapper: RepoListUiMapper,
    private val paginator: Paginator,
) : ViewModel() {

    private val _actions = Channel<RepoListAction>(capacity = BUFFERED)
    val actions = _actions.receiveAsFlow()

    private val query = MutableStateFlow(DEFAULT_QUERY)

    private val popupVisible = MutableStateFlow(false)
    private val sortType = MutableStateFlow<RepoSortType>(RepoSortType.Unknown())

    private val items = query
        .debounce(DEBOUNCE_TIMEOUT)
        .distinctUntilChanged()
        .filter(String::isNotEmpty)
        .flatMapLatest(paginator::getData)
        .stateIn(initialValue = emptyList())

    private val sortedItems = combine(
        items,
        sortType,
        sortRepos::invoke,
    )

    val state = combine(
        paginator.loadState,
        sortedItems,
        query,
        popupVisible,
        sortType,
        uiMapper::toUiState,
    ).stateIn(initialValue = RepoListState(query = DEFAULT_QUERY))

    fun onEvent(event: RepoListEvent) {
        when (event) {
            RepoListEvent.LoadMore -> paginator.nextPage()
            is RepoListEvent.OnQueryChanged -> this.query.update { event.query }
            is RepoListEvent.UpdateSortType -> viewModelScope.launch {
                sortType.update { event.repoSortType }
                _actions.send(RepoListAction.ScrollToTop)
            }
            RepoListEvent.SortMenuOptionsDismissed -> popupVisible.update { false }
            RepoListEvent.ToggleSortMenuOptionsVisibility -> popupVisible.update { !it }
            is RepoListEvent.OnItemClick -> viewModelScope.launch {
                _actions.send(RepoListAction.NavigateToDetails(event.authorImageUrl, event.repoFullName))
            }
            is RepoListEvent.OnImageClick -> viewModelScope.launch {
                _actions.send(RepoListAction.OpenProfile(event.profileUrl))
            }
            RepoListEvent.OnRetryClick -> viewModelScope.launch { paginator.retry() }
        }
    }
}

private const val DEBOUNCE_TIMEOUT = 500L
const val DEFAULT_QUERY = "kotlin"

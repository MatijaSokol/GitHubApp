package com.matijasokol.repo.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matijasokol.coreui.viewmodel.stateIn
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.domain.model.Repo
import com.matijasokol.repo.domain.usecase.SortReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val paginator: Paginator,
    private val sortRepos: SortReposUseCase,
) : ViewModel() {

    private val _actions = Channel<RepoListAction>(capacity = BUFFERED)
    val actions = _actions.receiveAsFlow()

    private val query = MutableStateFlow(DEFAULT_QUERY)

    private val popupVisible = MutableStateFlow(false)
    private val sortType = MutableStateFlow<RepoSortType>(RepoSortType.Unknown())

    private val items = query
        .debounce(500L)
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
        sortedItems.map { it.map(Repo::toRepoListItem).toPersistentList() },
        query,
        popupVisible,
        sortType,
        ::RepoListState,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = RepoListState(),
    )

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

const val DEFAULT_QUERY = "kotlin"

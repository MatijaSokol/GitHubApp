package com.matijasokol.ui_repolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matijasokol.repo_domain.usecase.FetchReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val fetchRepos: FetchReposUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RepoListState())
    val state = _state.asStateFlow()

    private val paginator = DefaultPaginator(
        initialKey = state.value.page,
        onLoadUpdated = { isLoading ->
            _state.update { it.copy(isLoading = isLoading) }
        },
        onRequest = { nextPage ->
            runCatching {
                fetchRepos.execute("kotlin", 30, nextPage)
            }
        },
        getNextKey = { state.value.page + 1 },
        onError = { ex -> _state.update { it.copy(
            error = ex?.localizedMessage
        ) } },
        onSuccess = { items, newKey ->
            _state.update { it.copy(
                items = (state.value.items + items).distinctBy { repo -> repo.id },
                page = newKey,
                endReached = items.isEmpty()
            ) }
        }
    )

    init {
        loadNextItems()
    }

    private fun loadNextItems() {
        viewModelScope.launch {
            if (state.value.endReached) return@launch
            paginator.loadNextItems()
        }
    }

    fun onEvent(event: RepoListEvent) {
        when (event) {
            RepoListEvent.LoadMore -> loadNextItems()
            is RepoListEvent.NavigateToRepoDetails -> {}
        }
    }
}
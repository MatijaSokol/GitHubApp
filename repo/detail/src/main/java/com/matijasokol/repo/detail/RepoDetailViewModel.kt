package com.matijasokol.repo.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.matijasokol.coreui.navigation.Destination
import com.matijasokol.repo.domain.usecase.GetRepoDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRepoDetails: GetRepoDetailsUseCase,
    private val uiMapper: RepoDetailsUiMapper,
) : ViewModel() {

    private val fetchTrigger = Channel<Unit>()
    private val isLoading = MutableStateFlow(true)

    private val repo = fetchTrigger.receiveAsFlow()
        .onStart { emit(Unit) }
        .map { savedStateHandle.toRoute<Destination.RepoDetail>().repoFullName }
        .onEach { isLoading.update { true } }
        .map(getRepoDetails::invoke)
        .onEach { isLoading.update { false } }

    val state = combine(
        isLoading,
        repo,
    ) { loading, repo ->
        with(savedStateHandle.toRoute<Destination.RepoDetail>()) {
            uiMapper.toUiState(loading, repo, repoFullName, authorImageUrl)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = with(savedStateHandle.toRoute<Destination.RepoDetail>()) {
            RepoDetailState.Loading(
                repoFullName = repoFullName,
                authorImageUrl = authorImageUrl,
            )
        },
    )
}

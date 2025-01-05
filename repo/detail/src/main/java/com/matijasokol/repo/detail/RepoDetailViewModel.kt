package com.matijasokol.repo.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.matijasokol.core.dictionary.Dictionary
import com.matijasokol.coreui.navigation.Destination
import com.matijasokol.coreui.viewmodel.stateIn
import com.matijasokol.repo.domain.usecase.GetRepoDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRepoDetails: GetRepoDetailsUseCase,
    private val uiMapper: RepoDetailsUiMapper,
    private val dictionary: Dictionary,
) : ViewModel() {

    private val _actions = Channel<RepoDetailAction>(capacity = BUFFERED)
    val actions = _actions.receiveAsFlow()

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
        initialValue = with(savedStateHandle.toRoute<Destination.RepoDetail>()) {
            RepoDetailState.Loading(
                repoFullName = repoFullName,
                authorImageUrl = authorImageUrl,
            )
        },
    )

    fun onEvent(event: RepoDetailEvent) {
        when (event) {
            RepoDetailEvent.OpenProfileWebError -> viewModelScope.launch {
                _actions.send(
                    RepoDetailAction.ShowMessage(
                        dictionary.getString(R.string.repo_detail_message_profile_browser_error),
                    ),
                )
            }
            RepoDetailEvent.OpenRepoWebError -> viewModelScope.launch {
                _actions.send(
                    RepoDetailAction.ShowMessage(
                        dictionary.getString(R.string.repo_detail_message_repo_browser_error),
                    ),
                )
            }
        }
    }
}

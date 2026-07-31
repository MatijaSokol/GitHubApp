package com.matijasokol.repo.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matijasokol.core.dictionary.Dictionary
import com.matijasokol.coreui.navigation.Destination
import com.matijasokol.coreui.viewmodel.stateIn
import com.matijasokol.repo.domain.usecase.GetRepoDetailsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = RepoDetailViewModel.Factory::class)
class RepoDetailViewModel @AssistedInject constructor(
    @Assisted private val destination: Destination.RepoDetail,
    getRepoDetails: GetRepoDetailsUseCase,
    uiMapper: RepoDetailsUiMapper,
    private val dictionary: Dictionary,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(destination: Destination.RepoDetail): RepoDetailViewModel
    }

    private val _actions = Channel<RepoDetailAction>(capacity = BUFFERED)
    val actions: Flow<RepoDetailAction> = _actions.receiveAsFlow()

    private val fetchTrigger = Channel<Unit>()
    private val isLoading = MutableStateFlow(true)

    private val repo = fetchTrigger.receiveAsFlow()
        .onStart { emit(Unit) }
        .onEach { isLoading.update { true } }
        .map { getRepoDetails(destination.repoFullName) }
        .onEach { isLoading.update { false } }

    val state: StateFlow<RepoDetailState> = combine(
        isLoading,
        repo,
    ) { loading, repo ->
        uiMapper.toUiState(loading, repo, destination.repoFullName, destination.authorImageUrl)
    }.stateIn(
        initialValue = RepoDetailState.Loading(
            repoFullName = destination.repoFullName,
            authorImageUrl = destination.authorImageUrl,
        ),
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

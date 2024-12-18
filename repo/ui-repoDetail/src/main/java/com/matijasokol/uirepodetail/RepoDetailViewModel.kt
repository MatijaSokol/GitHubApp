package com.matijasokol.uirepodetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.matijasokol.core.domain.Resource
import com.matijasokol.core.navigation.Destination
import com.matijasokol.coreui.dictionary.Dictionary
import com.matijasokol.repodomain.usecase.GetRepoDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRepoDetails: GetRepoDetailsUseCase,
    private val dictionary: Dictionary,
) : ViewModel() {

    private val fetchTrigger = Channel<Unit>()

    val state = fetchTrigger.receiveAsFlow()
        .onStart { emit(Unit) }
        .map { savedStateHandle.toRoute<Destination.RepoDetail>().repoId }
        .flatMapLatest(::repoDetailsFlow)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RepoDetailState(isLoading = true),
        )

    private fun repoDetailsFlow(repoId: Int) = getRepoDetails.execute(repoId)
        .map { resource ->
            when (resource) {
                is Resource.Error -> RepoDetailState(
                    errorMessage = dictionary.getString(R.string.repo_detail_message_cache_error, repoId),
                )
                is Resource.Loading -> RepoDetailState(isLoading = resource.isLoading)
                is Resource.Success -> RepoDetailState(repo = resource.data)
            }
        }
}

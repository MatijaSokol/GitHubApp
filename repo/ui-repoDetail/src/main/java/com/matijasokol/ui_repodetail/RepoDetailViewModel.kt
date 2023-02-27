package com.matijasokol.ui_repodetail

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matijasokol.core.domain.Resource
import com.matijasokol.repo_domain.usecase.GetRepoDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRepoDetails: GetRepoDetailsUseCase,
    private val context: Application
) : ViewModel() {

    private val _state = MutableStateFlow(RepoDetailState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<Int>(RepoDetailConstants.ARGUMENT_REPO_ID)?.let { repoId ->
            onEvent(RepoDetailEvent.GetRepoDetails(repoId))
        }
    }

    fun onEvent(event: RepoDetailEvent) {
        when (event) {
            is RepoDetailEvent.GetRepoDetails -> getRepoDetails(event.repoId)
        }
    }

    private fun getRepoDetails(repoId: Int) {
        getRepoDetails.execute(repoId).onEach { resource ->
            when (resource) {
                is Resource.Error -> _state.update { it.copy(errorMessage = context.getString(R.string.repo_detail_message_cache_error, repoId)) }
                is Resource.Loading -> _state.update { it.copy(isLoading = resource.isLoading) }
                is Resource.Success -> _state.update { it.copy(repo = resource.data) }
            }
        }.launchIn(viewModelScope)
    }
}
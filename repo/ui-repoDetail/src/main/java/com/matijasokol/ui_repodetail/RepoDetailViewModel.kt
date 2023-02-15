package com.matijasokol.ui_repodetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matijasokol.core.domain.Resource
import com.matijasokol.repo_domain.usecase.GetRepoFromCache
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
    private val getRepoFromCache: GetRepoFromCache
) : ViewModel() {

    private val _state = MutableStateFlow(RepoDetailState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<Int>("repoId")?.let { repoId ->
            onEvent(RepoDetailEvent.GetRepoFromCache(repoId))
        }
    }

    fun onEvent(event: RepoDetailEvent) {
        when (event) {
            is RepoDetailEvent.GetRepoFromCache -> getRepoFromCache(event.repoId)
        }
    }

    private fun getRepoFromCache(repoId: Int) {
        getRepoFromCache.execute(repoId).onEach { resource ->
            when (resource) {
                is Resource.Error -> _state.update { it.copy(message = "error") }
                is Resource.Loading -> _state.update { it }
                is Resource.Success -> _state.update { it.copy(message = resource.data.name) }
            }
        }.launchIn(viewModelScope)
    }
}
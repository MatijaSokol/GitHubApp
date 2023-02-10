package com.matijasokol.ui_repolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matijasokol.repo_domain.FetchReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val useCase: FetchReposUseCase
) : ViewModel() {

    private val _repos = MutableStateFlow(emptyList<String>())
    val repos = _repos.asStateFlow()

    init {
        viewModelScope.launch {
            val result = useCase.execute()
            _repos.value = result.map { it.name }
        }
    }
}
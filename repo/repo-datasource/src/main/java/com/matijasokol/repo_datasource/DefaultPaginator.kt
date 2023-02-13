package com.matijasokol.repo_datasource

import com.matijasokol.repo_domain.Paginator
import com.matijasokol.repo_domain.RepoService
import com.matijasokol.repo_domain.model.Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DefaultPaginator(
    private val repoService : RepoService
) : Paginator<Repo> {

    private val _state = MutableStateFlow(PaginatorState<Repo>())
    val state = _state.asStateFlow()

    override suspend fun loadNext(query: String) {
        if (query != state.value.query) {
            _state.update {
                it.copy(
                    pageLoaded = -1
                )
            }
        }

        _state.update { it.copy(isLoading = true) }

        try {
            val result = repoService.fetchRepos(
                query = query,
                perPage = 30,
                page = state.value.pageLoaded + 1
            )

            _state.update {
                it.copy(
                    items = it.items + result,
                    isLoading = false,
                    pageLoaded = it.pageLoaded + 1,
                    query = query
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = e
                )
            }
        }
    }

    override fun reset() {
        _state.update { PaginatorState() }
    }
}

data class PaginatorState<T>(
    val items: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val pageLoaded: Int = -1,
    val error: Exception? = null,
    val query: String = "kotlin"
)
package com.matijasokol.repo.domain

import com.matijasokol.repo.domain.model.Repo
import kotlinx.coroutines.flow.Flow

interface Paginator {

    enum class LoadState {
        Loaded,
        Refresh,
        Append,
        RefreshError,
        AppendError,
    }

    val loadState: Flow<LoadState>

    fun getData(query: String): Flow<List<Repo>>

    fun nextPage()

    suspend fun retry()
}

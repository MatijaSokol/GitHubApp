package com.matijasokol.repo.domain

import com.matijasokol.core.domain.Resource
import com.matijasokol.repo.domain.model.Repo
import kotlinx.coroutines.flow.Flow

interface Paginator {

    fun loadNext(query: String, shouldReset: Boolean): Flow<Resource<List<Repo>>>

    fun reset()
}

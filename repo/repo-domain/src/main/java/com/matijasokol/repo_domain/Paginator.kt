package com.matijasokol.repo_domain

import com.matijasokol.core.Resource
import com.matijasokol.repo_domain.model.Repo
import kotlinx.coroutines.flow.Flow

interface Paginator {

    fun loadNext(query: String, shouldReset: Boolean): Flow<Resource<List<Repo>>>

    fun reset()
}
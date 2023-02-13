package com.matijasokol.repo_domain.usecase

import com.matijasokol.repo_domain.Paginator

class FetchReposUseCase(
    private val paginator: Paginator
) {

    fun execute(query: String, shouldReset: Boolean) = paginator.loadNext(query, shouldReset)
}
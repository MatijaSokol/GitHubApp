package com.matijasokol.repo.domain.usecase

import com.matijasokol.repo.domain.Paginator
import javax.inject.Inject

class FetchReposUseCase @Inject constructor(
    private val paginator: Paginator,
) {

    fun execute(query: String, shouldReset: Boolean) = paginator.loadNext(query, shouldReset)
}

package com.matijasokol.repodomain.usecase

import com.matijasokol.repodomain.Paginator
import javax.inject.Inject

class FetchReposUseCase @Inject constructor(
    private val paginator: Paginator,
) {

    fun execute(query: String, shouldReset: Boolean) = paginator.loadNext(query, shouldReset)
}

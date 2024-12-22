package com.matijasokol.repo.domain.usecase

import arrow.core.Either
import com.matijasokol.core.error.NetworkError
import com.matijasokol.repo.domain.RepoService
import com.matijasokol.repo.domain.model.Repo
import javax.inject.Inject

class GetRepoDetailsUseCase @Inject constructor(
    private val repoService: RepoService,
) {

    suspend operator fun invoke(repoId: String): Either<NetworkError, Repo> =
        repoService.fetchRepoDetails(repoId)
}

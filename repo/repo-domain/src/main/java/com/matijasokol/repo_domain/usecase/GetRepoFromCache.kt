package com.matijasokol.repo_domain.usecase

import com.matijasokol.core.domain.Resource
import com.matijasokol.repo_domain.RepoCache
import com.matijasokol.repo_domain.model.Repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRepoFromCache(
    private val cache: RepoCache
) {

    fun execute(repoId: Int): Flow<Resource<Repo>> = flow {
        emit(Resource.Loading(true))

        try {
            val repo = cache.getRepo(repoId)
            emit(Resource.Success(repo))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e))
        } finally {
            emit(Resource.Loading(false))
        }
    }
}
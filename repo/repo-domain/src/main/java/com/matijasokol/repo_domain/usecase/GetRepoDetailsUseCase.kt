package com.matijasokol.repo_domain.usecase

import com.matijasokol.core.domain.Resource
import com.matijasokol.repo_domain.RepoCache
import com.matijasokol.repo_domain.RepoService
import com.matijasokol.repo_domain.model.Repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRepoDetailsUseCase @Inject constructor(
    private val repoService: RepoService,
    private val repoCache: RepoCache
) {

    fun execute(repoId: Int): Flow<Resource<Repo>> = flow {
        emit(Resource.Loading(true))

        try {
            val repo = repoCache.getRepo(repoId)

            val followersCount = repo.author.followersCount
            val reposCount = repo.author.reposCount

            val shouldMakeApiRequest = followersCount == null || reposCount == null

            if (!shouldMakeApiRequest) {
                emit(Resource.Success(repo))
                return@flow
            }

            try {
                val followers = repoService.fetchAuthorFollowers(repo.author.followersUrl)
                val repos = repoService.fetchAuthorRepos(repo.author.reposUrl)

                val followersCountNew = followers.distinctBy { it.id }.size
                val reposCountNew = repos.distinctBy { it.id }.size

                repoCache.updateFollowersCount(followersCountNew, repo.author.id)
                repoCache.updateReposCount(reposCountNew, repo.author.id)

                val updatedRepo = repoCache.getRepo(repoId)

                emit(Resource.Success(updatedRepo))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Success(repo))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e))
        } finally {
            emit(Resource.Loading(false))
        }
    }
}
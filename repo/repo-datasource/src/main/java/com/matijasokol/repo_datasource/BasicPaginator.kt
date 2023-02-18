package com.matijasokol.repo_datasource

import com.matijasokol.core.domain.Resource
import com.matijasokol.repo_domain.NetworkException
import com.matijasokol.repo_domain.Paginator
import com.matijasokol.repo_domain.ParseException
import com.matijasokol.repo_domain.RepoCache
import com.matijasokol.repo_domain.RepoService
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject

class BasicPaginator @Inject constructor(
    private val repoService: RepoService,
    private val repoCache: RepoCache
) : Paginator {

    private var lastQuery = ""
    private var lastPage = -1

    override fun loadNext(query: String, shouldReset: Boolean) = flow {
        if (shouldReset) reset()

        emit(Resource.Loading(true))

        try {
            val result = repoService.fetchRepos(
                query = query,
                perPage = 30,
                page = lastPage + 1
            )

            lastPage += 1
            lastQuery = query

            if (shouldReset) {
                repoCache.replaceRepos(result)
            } else {
                repoCache.insertRepos(result)
            }

            val cachedRepos = result.map { repoCache.getRepo(it.id) }

            emit(Resource.Success(cachedRepos))
        } catch (e: JsonConvertException) {
            e.printStackTrace()
            emit(Resource.Error(ParseException()))
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            emit(Resource.Error(NetworkException()))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e))
        } finally {
            emit(Resource.Loading(false))
        }
    }

    override fun reset() {
        lastQuery = ""
        lastPage = -1
    }
}
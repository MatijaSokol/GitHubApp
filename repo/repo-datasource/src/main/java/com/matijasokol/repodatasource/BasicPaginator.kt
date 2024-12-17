package com.matijasokol.repodatasource

import com.matijasokol.core.domain.Resource
import com.matijasokol.repodomain.NetworkException
import com.matijasokol.repodomain.Paginator
import com.matijasokol.repodomain.ParseException
import com.matijasokol.repodomain.RepoCache
import com.matijasokol.repodomain.RepoService
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject

class BasicPaginator @Inject constructor(
    private val repoService: RepoService,
    private val repoCache: RepoCache,
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
                page = lastPage + 1,
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

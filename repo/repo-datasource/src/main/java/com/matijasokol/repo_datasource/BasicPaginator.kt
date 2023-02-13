package com.matijasokol.repo_datasource

import com.matijasokol.core.Resource
import com.matijasokol.repo_domain.Paginator
import com.matijasokol.repo_domain.ParseException
import com.matijasokol.repo_domain.RepoService
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BasicPaginator @Inject constructor(
    private val repoService: RepoService
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

            emit(Resource.Success(result))
        } catch (e: JsonConvertException) {
            e.printStackTrace()
            emit(Resource.Error(ParseException()))
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
package com.matijasokol.repo.datasourcetest.network

sealed interface RepoServiceResponseType {

    data object EmptyList : RepoServiceResponseType

    data object MalformedData : RepoServiceResponseType

    data object GoodData : RepoServiceResponseType

    data object Http404 : RepoServiceResponseType
}

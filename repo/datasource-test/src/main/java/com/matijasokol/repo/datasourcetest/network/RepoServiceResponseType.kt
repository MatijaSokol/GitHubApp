package com.matijasokol.repo.datasourcetest.network

sealed interface RepoServiceResponseType {

    object EmptyList : RepoServiceResponseType

    object MalformedData : RepoServiceResponseType

    object GoodData : RepoServiceResponseType

    object Http404 : RepoServiceResponseType
}

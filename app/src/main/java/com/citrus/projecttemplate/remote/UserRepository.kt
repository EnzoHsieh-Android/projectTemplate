package com.citrus.projecttemplate.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val repository: MemeRepositoryImpl) {
    fun getSearchResults(query: String, time:String) =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                initialLoadSize = INIT_LOAD_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RepoPagingSource(repository, query, time) }
        ).flow

    companion object {
        const val NETWORK_PAGE_SIZE = 3
        const val INIT_LOAD_SIZE = 3
    }
}
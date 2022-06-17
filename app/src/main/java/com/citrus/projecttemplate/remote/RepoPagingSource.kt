package com.citrus.projecttemplate.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.citrus.projecttemplate.model.dto.User
import retrofit2.HttpException
import java.io.IOException

private const val USER_LIST_STARTING_PAGE_INDEX = 1

class RepoPagingSource(
    private val repository: MemeRepositoryImpl,
    private val query: String,
    private val currentTime: String
) : PagingSource<Int, User>() {

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val position = params.key ?: USER_LIST_STARTING_PAGE_INDEX


        return try {
            var users = repository.getUser(
                query = query,
                page = if (query.isNotBlank()) 0 else params.loadSize * (position - 1),
                itemPerPage = params.loadSize,
                currentTime = currentTime
            )


            val nextKey = if (users.isEmpty() || users.size < params.loadSize) {
                null
            } else {
                position.plus(1)
            }
            LoadResult.Page(
                data = users.distinctBy { it.id },
                prevKey = if (position == USER_LIST_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}
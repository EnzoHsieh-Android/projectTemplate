package com.citrus.projecttemplate.remote


import com.citrus.projecttemplate.model.dto.DataResponse
import com.citrus.projecttemplate.model.dto.User
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MemeRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : Repository {
    override fun getMeme() = resultFlowData(
        apiAction = { apiService.getMeme() },
        onSuccess = { result ->
            if (result.data.success) {
                Resource.Success(result.data.data.memes)
            } else {
                Resource.Error("no Data")
            }
        }
    )


    override fun getSample() = resultFlowData(
        apiAction = { apiService.getMeme() },
        onSuccess = { result ->
            if (result.data.success) {
                Resource.Success(result.data.data.memes)
            } else {
                Resource.Error("no Data")
            }
        }
    )


    override suspend fun getUser(query: String, page: Int, itemPerPage: Int, currentTime:String): List<User> {
        return apiService.getUser(query, page, itemPerPage,currentTime).body()?.users ?: listOf()
    }
}



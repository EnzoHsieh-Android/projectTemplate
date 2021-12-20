package com.citrus.projecttemplate.remote


import com.citrus.projecttemplate.model.dto.Meme
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MemeRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : Repository {
    override fun getMeme() = resultFlowData(
        apiQuery = { apiService.getMeme() },
        onSuccess = { result ->
            if (result.data.success) {
                Resource.Success(result.data.data.memes as MutableList)
            } else {
                Resource.Error("no Data")
            }
        }
    )

    override fun getSample() = resultFlowData(
        apiQuery = { apiService.getMeme() },
        onSuccess = { result ->
            if (result.data.success) {
                Resource.Success(result.data.data.memes as MutableList)
            } else {
                Resource.Error("no Data")
            }
        }
    )

}



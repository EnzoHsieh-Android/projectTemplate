package com.citrus.projecttemplate.remote


import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Repository @Inject constructor(
    private val apiService: ApiService
) {

    fun getMeme() = resultFlowData(
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



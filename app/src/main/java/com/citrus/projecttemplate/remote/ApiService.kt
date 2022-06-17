package com.citrus.projecttemplate.remote

import com.citrus.projecttemplate.model.dto.DataResponse
import com.citrus.projecttemplate.model.dto.User
import com.citrus.projecttemplate.model.dto.Welcome
import com.skydoves.sandwich.ApiResponse
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @GET("get_memes")
    suspend fun getMeme(): ApiResponse<Welcome>

    @GET("getUser")
    suspend fun getUser(
        @Query("query") query: String,
        @Query("offset") page: Int,
        @Query("limit") itemsPerPage: Int,
        @Query("currentTime") currentTime: String
    ): Response<DataResponse>
}
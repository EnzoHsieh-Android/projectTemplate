package com.citrus.projecttemplate.remote

import com.citrus.projecttemplate.model.dto.Welcome
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.*


interface ApiService {
    @GET("get_memes")
    suspend fun getMeme(): ApiResponse<Welcome>
}
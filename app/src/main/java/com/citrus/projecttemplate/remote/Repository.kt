package com.citrus.projecttemplate.remote

import com.citrus.projecttemplate.model.dto.DataResponse
import com.citrus.projecttemplate.model.dto.Meme
import com.citrus.projecttemplate.model.dto.User
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getMeme(): Flow<Resource<List<Meme>>>

    fun getSample(): Flow<Resource<List<Meme>>>

    suspend fun getUser(query:String, page:Int, itemPerPage:Int, currentTime:String): List<User>
}
package com.citrus.projecttemplate.remote

import com.citrus.projecttemplate.model.dto.Meme
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getMeme(): Flow<Resource<List<Meme>>>

    fun getSample(): Flow<Resource<List<Meme>>>
}
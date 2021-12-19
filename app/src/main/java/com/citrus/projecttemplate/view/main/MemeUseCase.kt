package com.citrus.projecttemplate.view.main

import com.citrus.projecttemplate.model.dto.Meme
import com.citrus.projecttemplate.remote.MemeRepositoryImpl
import com.citrus.projecttemplate.remote.Resource
import com.citrus.projecttemplate.util.base.BaseUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MemeUseCase  @Inject constructor(
    private val repository: MemeRepositoryImpl
): BaseUseCase<Resource<MutableList<Meme>>>() {

    override fun performAction(): Flow<Resource<MutableList<Meme>>> {
       return repository.getMeme()
    }

}
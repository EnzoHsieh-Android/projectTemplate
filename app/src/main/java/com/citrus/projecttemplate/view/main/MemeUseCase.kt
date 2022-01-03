package com.citrus.projecttemplate.view.main


import com.citrus.projecttemplate.remote.MemeRepositoryImpl
import com.citrus.projecttemplate.remote.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class MemeUseCase @Inject constructor(
    private val repository: MemeRepositoryImpl
) {
    operator fun invoke() = repository.getMeme()

     var job: Job? = null


     fun mergeResult() = combine(
             repository.getSample(),
             repository.getMeme(),
             transform = { orgResult, newResult -> Pair(orgResult, newResult) }
         ).map { (orgResult, newResult) ->

         }


}
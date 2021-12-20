package com.citrus.projecttemplate.view.main


import com.citrus.projecttemplate.remote.MemeRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MemeUseCase @Inject constructor(
    private val repository: MemeRepositoryImpl
) {
    operator fun invoke() = repository.getMeme()



    fun mergeResult(a:String , c:Int) =
        combine(
            repository.getSample(),
            repository.getMeme(),
            transform = { orgResult, newResult -> Pair(orgResult, newResult) }
        ).map { (orgResult, newResult) ->

        }


}
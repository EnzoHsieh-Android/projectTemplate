package com.citrus.projecttemplate.remote

import android.util.Log
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.lang.Exception

sealed class Resource<out T>(val data: T? = null, val message: String? = null,val isLoading:Boolean = false) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(message: String) : Resource<T>(message = message)
    class Loading<T>(isLoading:Boolean) : Resource<T>(isLoading = isLoading)
}

class RetryCondition(val errorMsg: String) : Exception()

/**基於sandwich進一步封裝含retry功能、error錯誤處理,僅抽出success各自實作*/
fun <T, DATA> resultFlowData(
    apiQuery: suspend () -> ApiResponse<T>,
    onSuccess: (ApiResponse.Success<T>) -> Resource<MutableList<DATA>>
) = flow {
    apiQuery.invoke().suspendOnSuccess {
        emit(onSuccess(this))
    }.suspendOnError {
        throw RetryCondition(this.statusCode.name)
    }.suspendOnException {
        throw RetryCondition(this.exception.message!!)
    }
}.retryWhen { cause, attempt ->
    if (cause is RetryCondition && attempt < 3) {
        delay(1500)
        return@retryWhen true
    } else {
        emit(Resource.Error(cause.message!!))
        return@retryWhen false
    }
}.catch {
    Log.e("catch error", "--")
}.flowOn(Dispatchers.IO)




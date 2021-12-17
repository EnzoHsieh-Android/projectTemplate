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

sealed class Resource<out T>(
    val status: Status,
    val _data: T?,
    val message: String?,
    val loading: Boolean?
) {
    enum class Status {
        LOADING,
        SUCCESS,
        ERROR
    }

    data class Success<out R>(val data: R) : Resource<R>(
        status = Status.SUCCESS,
        _data = data,
        message = null,
        loading = null
    )

    data class Loading(val isLoading: Boolean) : Resource<Nothing>(
        status = Status.LOADING,
        _data = null,
        message = null,
        loading = isLoading
    )

    data class Error(val exception: String) : Resource<Nothing>(
        status = Status.ERROR,
        _data = null,
        message = exception,
        loading = null
    )
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
}.onStart { emit(Resource.Loading(true)) }
    .onCompletion { emit(Resource.Loading(false)) }
    .flowOn(Dispatchers.IO)




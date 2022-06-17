package com.citrus.projecttemplate

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.citrus.projecttemplate.model.dto.User
import com.citrus.projecttemplate.remote.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
class BlankViewModel @Inject constructor(val repository: UserRepository) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    val time: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    private val actionStateFlow = MutableSharedFlow<String>()
    private val searches = actionStateFlow
        /**避免重複的搜索請求。假設正在搜索 Android，用戶刪除了 d  然後輸入 d。最後的結果還是 Android。它就不會再執行搜索查詢*/
        .distinctUntilChanged()
        .onStart { emit("") }

    @RequiresApi(Build.VERSION_CODES.O)
    var newResult = searches.flatMapLatest {
        repository.getSearchResults(it, time)
    }.distinctUntilChanged()


    fun searchByName(query: String) = viewModelScope.launch {
        Log.e("query", query)
        actionStateFlow.emit(query)
    }


}
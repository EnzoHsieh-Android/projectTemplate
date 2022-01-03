package com.citrus.projecttemplate.view.main.dialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.projecttemplate.view.main.DialogType
import com.citrus.projecttemplate.view.main.MemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor(
    private val useCase: MemeUseCase
) : ViewModel() {

    private val _sharedPackString = MutableSharedFlow<String>(replay = 1)
    val sharedPackString: SharedFlow<String> = _sharedPackString

    private val _result = MutableSharedFlow<Boolean>()
    val result: SharedFlow<Boolean> = _result

    fun initLaunch() = viewModelScope.launch {
        Log.e("ViewModel", "trigger")

        _sharedPackString.emit("Hello From ViewModel")
        _result.emit(true)
    }

}


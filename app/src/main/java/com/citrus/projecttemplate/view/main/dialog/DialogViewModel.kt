package com.citrus.projecttemplate.view.main.dialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.projecttemplate.view.main.SharedPack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor(
    private val sharedPack: SharedPack
): ViewModel() {

    private val _sharedPackString = MutableSharedFlow<String> ()
    val sharedPackString: SharedFlow<String> = _sharedPackString

    fun initLaunch() {
        viewModelScope.launch {
            Log.e("test", sharedPack.sharedInformation)
           // _sharedPackString.emit(sharedPack.sharedInformation)
        }
    }

}
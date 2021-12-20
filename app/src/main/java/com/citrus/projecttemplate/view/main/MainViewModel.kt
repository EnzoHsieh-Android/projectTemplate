package com.citrus.projecttemplate.view.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.projecttemplate.model.dto.Meme
import com.citrus.projecttemplate.model.dto.PuzzleBitmap
import com.citrus.projecttemplate.remote.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import java.util.Collections.shuffle
import javax.inject.Inject

sealed class DialogType {
    object AlertDialog : DialogType()
    object DialogFragment : DialogType()
}

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: MemeUseCase
) : ViewModel() {

    private val _result = MutableSharedFlow<DialogType>()
    val result: SharedFlow<DialogType> = _result

    private val _memeList = MutableStateFlow<Resource<MutableList<Meme>>>(Resource.Loading())
    val memeList: StateFlow<Resource<MutableList<Meme>>> = _memeList

    private val _memePicUrl = MutableSharedFlow<String> ()
    val memePicUrl: SharedFlow<String> = _memePicUrl

    private val _puzzle = MutableSharedFlow<List<PuzzleBitmap>>()
    val puzzle: SharedFlow<List<PuzzleBitmap>> = _puzzle

    private val _loadingStatus = MutableSharedFlow<Boolean>()
    val loadingStatus: SharedFlow<Boolean> = _loadingStatus



    fun initLaunch() {
        viewModelScope.launch {
            useCase.mergeResult("",1).collect {

            }
            useCase().collect {
                _memeList.emit(it)
            }
        }
    }

    fun clickEvent(dialogType: DialogType) = viewModelScope.launch {
        _result.emit(dialogType)
    }


    fun onItemClicked() = viewModelScope.launch {
        Log.e("ViewModel trigger", "do something")
    }

    fun startPuzzle(puzzleList: List<PuzzleBitmap>) = viewModelScope.launch {
        val list = puzzleList.map { it.copy() }
        shuffle(list)
        _puzzle.emit(list)
    }

    fun getRandomPic() = viewModelScope.launch{
        val list = _memeList.value.data
        if(list?.isNotEmpty() == true) {
            val meme = list[(0 until list.size).random()]
            _memePicUrl.emit(meme.url)
        }
    }


}
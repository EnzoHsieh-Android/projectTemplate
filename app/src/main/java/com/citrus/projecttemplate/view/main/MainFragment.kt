package com.citrus.projecttemplate.view.main

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.citrus.projecttemplate.databinding.FragmentMainBinding
import com.citrus.projecttemplate.remote.Resource
import com.citrus.projecttemplate.util.Constants
import com.citrus.projecttemplate.util.Constants.clickAnimation
import com.citrus.projecttemplate.util.base.BindingFragment
import com.citrus.projecttemplate.util.base.lifecycleFlow
import com.citrus.projecttemplate.util.base.lifecycleLatestFlow
import com.citrus.projecttemplate.util.ext.onSafeClick
import com.citrus.projecttemplate.util.navigateSafely
import com.citrus.projecttemplate.view.main.adapter.DemoItemAdapter
import com.citrus.projecttemplate.view.main.adapter.PuzzleAdapter
import com.citrus.projecttemplate.view.main.dialog.OrangeAlertDialog
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import javax.inject.Inject


import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.citrus.projecttemplate.R
import com.citrus.projecttemplate.model.dto.PuzzleBitmap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : BindingFragment<FragmentMainBinding>(){
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMainBinding::inflate


    private val viewModel: MainViewModel by viewModels()
    private var job: Job? = null
    var orangeDialog: OrangeAlertDialog? = null
    private var puzzleList: MutableList<PuzzleBitmap>? = null

    @Inject
    lateinit var demoItemAdapter: DemoItemAdapter

    @Inject
    lateinit var puzzleAdapter: PuzzleAdapter



    @RequiresApi(Build.VERSION_CODES.R)
    override fun initView() {
        viewModel.initLaunch()

        binding.apply {
            demoRv.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = puzzleAdapter
            }


            refresh.setOnRefreshListener {
                viewModel.getRandomPic()
            }


            btn.onSafeClick { v ->
                v.clickAnimation {
                    viewModel.clickEvent(DialogType.AlertDialog)
                }
            }

            btn2.onSafeClick { v ->
                v.clickAnimation {
                    viewModel.clickEvent(DialogType.DialogFragment)
                }
            }

            start.onSafeClick { v ->
                v.clickAnimation {
                    viewModel.startPuzzle(puzzleList!!)
                }
            }
        }
    }


    override fun initObserve() {

        lifecycleFlow(viewModel.puzzle) { list ->
            job?.cancel()
            job =
                lifecycleScope.launch { puzzleAdapter.updateDataset(list as MutableList<PuzzleBitmap>) }
        }

        lifecycleFlow(viewModel.loadingStatus) { status ->
            binding.progress.isVisible = status
        }

        lifecycleLatestFlow(viewModel.memeList) { state ->
            when (state) {
                is Resource.Success -> {
                    if (state.data!!.isNotEmpty()) {
                        binding.progress.isVisible = false
                        viewModel.getRandomPic()
                    }
                }
                is Resource.Error -> {
                    Log.e("error", state.message!!)
                }
                is Resource.Loading ->  binding.progress.isVisible = true
            }
        }

        lifecycleFlow(viewModel.memePicUrl) { url ->
            if (url.isNotBlank()) {
                binding.refresh.isRefreshing = false
                Glide.with(this)
                    .asBitmap()
                    .load(url)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            puzzleList = mutableListOf()
                            for (i in 0..2) {
                                var yValue = when (i) {
                                    0 -> 0
                                    1 -> resource.height / 3
                                    2 -> (resource.height / 3) * 2
                                    else -> 0
                                }
                                for (j in 0..2) {
                                    var xValue = when (j) {
                                        0 -> 0
                                        1 -> resource.width / 3
                                        2 -> (resource.width / 3) * 2
                                        else -> 0
                                    }
                                    val bmp: Bitmap = Bitmap.createBitmap(
                                        resource, xValue, yValue, resource.width / 3,
                                        resource.height / 3
                                    )
                                    puzzleList?.add(
                                        PuzzleBitmap(
                                            bmp,
                                            puzzleList?.size?.plus(1)!!
                                        )
                                    )
                                    job?.cancel()
                                    job =
                                        lifecycleScope.launch {
                                            puzzleAdapter.updateDataset(
                                                puzzleList!!
                                            )
                                        }
                                }
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            // this is called when imageView is cleared on lifecycle call or for
                            // some other reason.
                            // if you are referencing the bitmap somewhere else too other than this imageView
                            // clear it here as you can no longer have the bitmap
                        }
                    })
            }
        }

        lifecycleFlow(viewModel.result) { type ->
            when (type) {
                is DialogType.AlertDialog -> {
                    orangeDialog?.dismiss()
                    orangeDialog = OrangeAlertDialog(requireActivity())
                    orangeDialog?.show()
                    orangeDialog?.window?.setLayout(
                        (Constants.screenW * 0.5).toInt(),
                        (Constants.screenH * 0.4).toInt()
                    )
                }
                is DialogType.DialogFragment -> {
                    findNavController().navigateSafely(R.id.action_mainFragment_to_orangeDialogFragment)
                }
            }
        }


    }

    override fun initAction() {
        initDemoAdapter()
        initPuzzleAdapter()
    }

    private fun initPuzzleAdapter() {
        puzzleAdapter.setOnCompleteListener {
            viewModel.clickEvent(DialogType.AlertDialog)
        }
    }

    /**DemoAdapter action*/
    private fun initDemoAdapter() {
        demoItemAdapter.setOnImgClickListener {
            YoYo.with(Techniques.BounceInUp).duration(700).playOn(it)
        }
        demoItemAdapter.setOnItemClickListener { viewModel.onItemClicked() }
        demoItemAdapter.setOnItemRemovedListener { meme -> Log.e("remove meme", meme.toString()) }
    }

    override fun onDestroyView() {
        orangeDialog = null
        super.onDestroyView()
    }
}
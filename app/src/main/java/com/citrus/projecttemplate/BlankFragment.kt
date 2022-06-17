package com.citrus.projecttemplate


import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.citrus.projecttemplate.databinding.FragmentBlankBinding
import com.citrus.projecttemplate.util.base.BindingFragment
import com.citrus.projecttemplate.view.main.adapter.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class BlankFragment : BindingFragment<FragmentBlankBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBlankBinding::inflate

    private val viewModel: BlankViewModel by viewModels()
    private val adapter: UserAdapter = UserAdapter()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {

        binding.apply {
            rvEmp.setHasFixedSize(true)
            rvEmp.itemAnimator = null
            rvEmp.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            rvEmp.adapter = adapter


            btnSearch.setOnClickListener {
                viewModel.searchByName(etSearch.text.toString())
            }
        }

        adapter.addLoadStateListener { loadState ->
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                    adapter.refresh()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.newResult.collectLatest {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
    }

    override fun initAction() {

    }

}
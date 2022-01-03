package com.citrus.projecttemplate.util.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


abstract class BindingFragment<out T : ViewBinding> : Fragment() {
    protected abstract val bindingInflater: (LayoutInflater) -> ViewBinding
    private var _binding: ViewBinding? = null
    @Suppress("UNCHECKED_CAST")
    protected val binding: T
        get() = _binding as T


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = bindingInflater(inflater)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAction()
        initObserve()
    }

    abstract fun initView()
    abstract fun initObserve()
    abstract fun initAction()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/**StateFLow使用*/
fun <T> Fragment.lifecycleFlow(flow: Flow<T>, action: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            /**update coroutine to 1.6.0 change this collect behavior,keep watch this issue*/
            flow.collect{ action(it) }
        }
    }
}

/**SharedFlow使用*/
fun <T> Fragment.lifecycleLatestFlow(flow: Flow<T>, action: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(action)
        }
    }
}
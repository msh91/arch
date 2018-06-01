package io.github.msh91.arch.ui.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseFragment<V : BaseViewModel, B : ViewDataBinding> : Fragment(), BaseView<V, B> {

    override lateinit var binding: B

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * viewModel will be created by the first time it's called via [viewModelFactory] and it's class type
     * will be found by Java Reflection
     */
    override val viewModel: V by lazy {
        @Suppress("UNCHECKED_CAST")
        ViewModelProviders.of(this, viewModelFactory).get((javaClass
                .genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<V>)
    }

    override fun onAttach(context: Context?) {
        // we should inject fragment dependencies before invoking super.onAttach()
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // initialize binding
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.setLifecycleOwner(this)

        // set viewModel as an observer to this activity lifecycle events
        lifecycle.addObserver(viewModel)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // observe viewModel uiActions in order to pass parent activity as argument of uiAction
        viewModel.uiActionLiveData.observe(this, Observer { it?.invoke(requireActivity()) })
        onViewInitialized(binding)
    }
}
package io.github.msh91.arch.ui.base

import android.arch.lifecycle.ViewModelProvider
import android.databinding.ViewDataBinding

/**
 * Created by m.aghajani on 2/22/2018.
 */
interface BaseView<V : BaseViewModel, B : ViewDataBinding> {

    val viewModelFactory: ViewModelProvider.Factory
    val viewModel: V
    val layoutId: Int
    var binding: B

    fun onViewInitialized(binding: B) {}
}
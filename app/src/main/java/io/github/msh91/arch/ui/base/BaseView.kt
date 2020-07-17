package io.github.msh91.arch.ui.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider

/**
 * Base interaction for every View such as activities, fragments, dialogs
 */
interface BaseView<V : BaseViewModel, B : ViewDataBinding> {

    /**
     * will be used for getting instance of ViewModels
     */
    val viewModelFactory: ViewModelProvider.Factory

    /**
     * default ViewModel of view, will be initialized by [viewModelFactory]
     */
    val viewModel: V

    /**
     * Resource Id of main layout for view
     */
    val layoutId: Int

    /**
     * child class of [ViewDataBinding] that generated for this [layoutId] by Android Data Binding framework
     */
    var binding: B

    /**
     * will be called after intialization of view
     *
     * @param binding refers to [binding]
     */
    fun onViewInitialized(binding: B) {}
}

package io.github.msh91.arch.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import io.github.msh91.arch.ui.base.ViewModelScope.ACTIVITY
import io.github.msh91.arch.ui.base.ViewModelScope.FRAGMENT
import javax.inject.Inject

abstract class BaseFragment<V : BaseViewModel, B : ViewDataBinding> : DaggerFragment(), BaseView<V, B> {

    override lateinit var binding: B

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Attempt to get viewModel lazily from [viewModelFactory] with the scope of given activity.
     *
     * @param activity given scope.
     * @return T an instance of requested ViewModel.
     */
    inline fun <reified T : BaseViewModel> getLazyViewModel(scope: ViewModelScope): Lazy<T> =
        lazy {
            when (scope) {
                ACTIVITY -> ViewModelProviders.of(requireActivity(), viewModelFactory)[T::class.java]
                FRAGMENT -> ViewModelProviders.of(this, viewModelFactory)[T::class.java]
            }
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
        viewModel.activityAction.observe(viewLifecycleOwner, Observer { it?.invoke(requireActivity()) })
        viewModel.fragmentAction.observe(viewLifecycleOwner, Observer { it?.invoke(this) })
        onViewInitialized(binding)
    }
}

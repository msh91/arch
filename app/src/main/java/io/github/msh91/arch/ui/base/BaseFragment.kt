package io.github.msh91.arch.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import io.github.msh91.arch.ui.base.ViewModelScope.ACTIVITY
import io.github.msh91.arch.ui.base.ViewModelScope.FRAGMENT
import javax.inject.Inject

abstract class BaseFragment<V : BaseViewModel, B : ViewDataBinding> : DaggerFragment(), BaseView<V, B> {

    private var _binding: B? = null
    override val binding: B
        get() = _binding
            ?: throw IllegalStateException("access to binding should between onCreateView and onDestroyView")

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Attempt to get viewModel lazily from [viewModelFactory] with the scope of given activity.
     *
     * @param scope given scope.
     * @return T an instance of requested ViewModel.
     */
    inline fun <reified T : BaseViewModel> getLazyViewModel(scope: ViewModelScope): Lazy<T> =
        lazy {
            when (scope) {
                ACTIVITY -> ViewModelProvider(requireActivity(), viewModelFactory)[T::class.java]
                FRAGMENT -> ViewModelProvider(this, viewModelFactory)[T::class.java]
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // initialize binding
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.unbind()
        _binding = null
    }
}

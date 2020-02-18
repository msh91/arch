package io.github.msh91.arch.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * Every Activity should inherit from this base activity in order to create relevant binding class,
 * inject dependencies and handling default actions.
 * @param V A ViewModel class that inherited from [BaseViewModel], will be used as default ViewModel of activity
 * @param B A Binding class that inherited from [ViewDataBinding], will be used for creating View of this activity
 */
abstract class BaseActivity<V : BaseViewModel, B : ViewDataBinding> : AppCompatActivity(), BaseView<V, B>, HasAndroidInjector {
    override lateinit var binding: B

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Attempt to get viewModel lazily from [viewModelFactory] with the scope of given activity.
     *
     * @return T an instance of requested ViewModel.
     */
    inline fun <reified T : BaseViewModel> getLazyViewModel(): Lazy<T> =
            lazy { ViewModelProviders.of(this, viewModelFactory)[T::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        // we should inject dependencies before invoking super.onCreate()
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        // initialize binding
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        // set viewModel as an observer to this activity lifecycle events
        lifecycle.addObserver(viewModel)
        // observe viewModel uiActions in order to pass this activity as argument of uiAction
        viewModel.activityAction.observe(this, Observer { it?.invoke(this) })
        onViewInitialized(binding)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}


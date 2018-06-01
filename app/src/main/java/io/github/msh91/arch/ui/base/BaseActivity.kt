package io.github.msh91.arch.ui.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

/**
 * Every Activity should inherit from this base activity in order to create relevant binding class,
 * inject dependencies and handling default actions.
 * @param V A ViewModel class that inherited from [BaseViewModel], will be used as default ViewModel of activity
 * @param B A Binding class that inherited from [ViewDataBinding], will be used for creating View of this activity
 */
abstract class BaseActivity<V : BaseViewModel, B : ViewDataBinding> : AppCompatActivity(), BaseView<V, B>, HasSupportFragmentInjector {
    override lateinit var binding: B

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

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

    override fun onCreate(savedInstanceState: Bundle?) {
        // we should inject dependencies before invoking super.onCreate()
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        // initialize binding
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.setLifecycleOwner(this)

        // set viewModel as an observer to this activity lifecycle events
        lifecycle.addObserver(viewModel)
        //todo:  viewModel.checkConnection()
        // observe viewModel uiActions in order to pass this activity as argument of uiAction
        viewModel.uiActionLiveData.observe(this, Observer { it?.invoke(this) })

        onViewInitialized(binding)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector
}


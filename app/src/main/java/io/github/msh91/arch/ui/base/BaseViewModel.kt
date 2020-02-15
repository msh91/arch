package io.github.msh91.arch.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import io.github.msh91.arch.data.mapper.ErrorMapper
import io.github.msh91.arch.data.model.response.ErrorModel
import io.github.msh91.arch.util.connectivity.BaseConnectionManager
import io.github.msh91.arch.util.livedata.ActivityActionLiveData
import io.github.msh91.arch.util.livedata.FragmentActionLiveData
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * All of ViewModels should be inherited from [BaseViewModel]
 *
 * @param connectionManager an instance of provided [BaseConnectionManager] to check connection status
 * before api calls
 */
abstract class BaseViewModel(private val errorMapper: ErrorMapper)
    : ViewModel(), LifecycleObserver {

    val activityAction = ActivityActionLiveData()
    val fragmentAction = FragmentActionLiveData()

    protected fun handleException(handler: (ErrorModel) -> Unit): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            handler.invoke(errorMapper.getErrorModel(throwable))
        }
    }

    /**
     * We can use lifeCycle in inherited classes if we need
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {}

    /**
     * We can use lifeCycle in inherited classes if we need
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {}
}
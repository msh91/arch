package io.github.msh91.arch.ui.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.github.msh91.arch.util.connectivity.BaseConnectionManager
import io.github.msh91.arch.util.livedata.ActivityActionLiveData
import io.github.msh91.arch.util.livedata.FragmentActionLiveData
import io.reactivex.disposables.CompositeDisposable

/**
 * All of ViewModels should be inherited from [BaseViewModel]
 *
 * @param connectionManager an instance of provided [BaseConnectionManager] to check connection status
 * before api calls
 */
abstract class BaseViewModel(private val connectionManager: BaseConnectionManager)
    : ViewModel(), LifecycleObserver {

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val activityAction = ActivityActionLiveData()
    val fragmentAction = FragmentActionLiveData()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun checkConnection() {
        if (connectionManager.isVPNConnected() == true)
            Log.d("VPN Status", "Connected")
        else
            Log.d("VPN Status", "Nothing")
    }

    fun onTokenExpired() {
        // todo : what should i do if token expired ?!
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
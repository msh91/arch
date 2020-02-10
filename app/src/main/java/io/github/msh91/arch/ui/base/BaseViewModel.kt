package io.github.msh91.arch.ui.base

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import io.github.msh91.arch.util.connectivity.BaseConnectionManager
import io.github.msh91.arch.util.livedata.ActivityActionLiveData
import io.github.msh91.arch.util.livedata.FragmentActionLiveData

/**
 * All of ViewModels should be inherited from [BaseViewModel]
 *
 * @param connectionManager an instance of provided [BaseConnectionManager] to check connection status
 * before api calls
 */
abstract class BaseViewModel(private val connectionManager: BaseConnectionManager)
    : ViewModel(), LifecycleObserver {

    val activityAction = ActivityActionLiveData()
    val fragmentAction = FragmentActionLiveData()

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
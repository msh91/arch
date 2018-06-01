package io.github.msh91.arch.util.livedata

import android.support.v4.app.FragmentActivity

/**
 * A lambda function that receives a [FragmentActivity]
 */
typealias UiAction = (FragmentActivity) -> Unit

/**
 * A custom wrapper for [SingleEventLiveData] that only works with [UiAction]
 */
class UiActionLiveData: SingleEventLiveData<UiAction>() {

    /**
     * invoke operator function to save [action] to value of [SingleEventLiveData] instance.
     *
     * <br></br>
     * For Example:
     *
     * val uiActionLiveData = UiActionLiveData()
     *
     * uiActionLiveData { activity -> // do something with given activity }
     *
     * @param action a lambda function that receives a [FragmentActivity].
     *
     */
    operator fun invoke(action: UiAction) {
        this.value = action
    }
}
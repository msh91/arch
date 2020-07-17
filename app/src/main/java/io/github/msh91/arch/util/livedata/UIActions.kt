package io.github.msh91.arch.util.livedata

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * A lambda function that receives a [FragmentActivity]
 */
typealias ActivityAction = (FragmentActivity) -> Unit

/**
 * A custom wrapper for [SingleEventLiveData] that only works with [ActivityAction]
 */
class ActivityActionLiveData : SingleEventLiveData<ActivityAction>() {

    /**
     * invoke operator function to save [action] to value of [SingleEventLiveData] instance.
     *
     * <br></br>
     * For Example:
     *
     * val activityActionLiveData = ActivityActionLiveData()
     *
     * activityActionLiveData { activity -> // do something with given activity }
     *
     * @param action a lambda function that receives a [FragmentActivity].
     *
     */
    operator fun invoke(action: ActivityAction) {
        this.value = action
    }
}

/**
 * A lambda function that receives a [Fragment]
 */
typealias FragmentAction = (androidx.fragment.app.Fragment) -> Unit

/**
 * A custom wrapper for [SingleEventLiveData] that only works with [FragmentAction]
 */
class FragmentActionLiveData : SingleEventLiveData<FragmentAction>() {

    /**
     * invoke operator function to save [action] to value of [SingleEventLiveData] instance.
     *
     * <br></br>
     * For Example:
     *
     * val fragmentActionLiveData = FragmentActionLiveData()
     *
     * fragmentActionLiveData { fragment -> // do something with the given fragment }
     *
     * @param action a lambda function that receives a [FragmentAction].
     *
     */
    operator fun invoke(action: FragmentAction) {
        this.value = action
    }
}

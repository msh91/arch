package io.github.msh91.arch.util.livedata

import android.support.v4.app.FragmentActivity

typealias UiAction = (FragmentActivity) -> Unit

class UiActionLiveData: SingleEventLiveData<UiAction>() {

    operator fun invoke(action: UiAction) {
        this.value = action
    }
}
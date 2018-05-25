package io.github.msh91.arch.ui.base.comman

import android.arch.lifecycle.MutableLiveData
import android.support.v4.app.FragmentActivity

typealias UiAction = (FragmentActivity) -> Unit

class UiActionLiveData: MutableLiveData<UiAction>() {

    operator fun invoke(action: UiAction) {
        this.value = action
    }
}
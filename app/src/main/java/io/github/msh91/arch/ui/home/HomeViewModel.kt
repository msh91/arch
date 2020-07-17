package io.github.msh91.arch.ui.home

import io.github.msh91.arch.ui.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeNavigator: HomeNavigator
) : BaseViewModel() {

    override fun onStart() {
        super.onStart()
        activityAction { homeNavigator.openListFragment(it) }
    }
}

package io.github.msh91.arch.ui.home

import io.github.msh91.arch.ui.base.BaseViewModel
import io.github.msh91.arch.util.connectivity.BaseConnectionManager
import javax.inject.Inject

class HomeViewModel @Inject constructor(
        private val homeNavigator: HomeNavigator,
        connectionManager: BaseConnectionManager
) : BaseViewModel(connectionManager) {

    override fun clearUseCaseDisposables() {}

    override fun onStart() {
        super.onStart()
        homeNavigator.openListFragment()
    }
}
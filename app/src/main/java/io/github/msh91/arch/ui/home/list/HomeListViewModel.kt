package io.github.msh91.arch.ui.home.list

import io.github.msh91.arch.ui.base.BaseViewModel
import io.github.msh91.arch.util.connectivity.BaseConnectionManager
import javax.inject.Inject

class HomeListViewModel @Inject constructor(
        connectionManager: BaseConnectionManager
) : BaseViewModel(connectionManager) {
    private val TAG = HomeListViewModel::class.java.simpleName
}
package io.github.msh91.arch.ui.home

import androidx.fragment.app.Fragment
import io.github.msh91.arch.R
import io.github.msh91.arch.ui.base.BaseNavigator
import javax.inject.Inject

class HomeNavigator @Inject constructor() : BaseNavigator {

    fun navigateToChartFragment(fragment: Fragment) {
        navigateTo(fragment, R.id.latestUpdatesToChartAction)
    }
}

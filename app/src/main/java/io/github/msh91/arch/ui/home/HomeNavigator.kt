package io.github.msh91.arch.ui.home

import io.github.msh91.arch.R
import io.github.msh91.arch.ui.home.list.HomeListFragment
import io.github.msh91.arch.util.navigator.BaseNavigator
import java.lang.ref.WeakReference

class HomeNavigator(homeActivity: HomeActivity) : BaseNavigator<HomeActivity> {

    override val mActivity: WeakReference<HomeActivity> = WeakReference(homeActivity)

    fun openListFragment() {
        sFragmentManager?.let {
            it
                    .beginTransaction()
                    .add(R.id.home_container, HomeListFragment.newInstance())
                    .commitAllowingStateLoss()
        }
    }
}
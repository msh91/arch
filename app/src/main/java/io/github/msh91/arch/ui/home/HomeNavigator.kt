package io.github.msh91.arch.ui.home

import android.support.v4.app.FragmentActivity
import io.github.msh91.arch.R
import io.github.msh91.arch.ui.base.BaseNavigator
import io.github.msh91.arch.ui.home.list.HomeListFragment
import javax.inject.Inject

class HomeNavigator @Inject constructor(): BaseNavigator {

//    override val mActivity: WeakReference<HomeActivity> = WeakReference(homeActivity)

    fun openListFragment(activity: FragmentActivity) {
        activity.supportFragmentManager
                .beginTransaction()
                .add(R.id.home_container, HomeListFragment.newInstance())
                .commitAllowingStateLoss()

    }
}
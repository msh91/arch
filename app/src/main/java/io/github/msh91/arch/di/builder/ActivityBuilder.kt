package io.github.msh91.arch.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.msh91.arch.ui.home.HomeActivity
import io.github.msh91.arch.ui.home.HomeActivityModule
import io.github.msh91.arch.ui.home.HomeFragmentProvider

/**
 * The Main Module for binding all of activities.
 * Every Activity should contribute with it's related modules
 */
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [(HomeActivityModule::class), (HomeFragmentProvider::class)])
    internal abstract fun bindHomeActivity(): HomeActivity
}

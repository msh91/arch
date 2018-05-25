package io.github.msh91.arch.ui.home

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.msh91.arch.ui.home.list.HomeListFragment
import io.github.msh91.arch.ui.home.list.HomeListModule

@Module
abstract class HomeFragmentProvider {

    @ContributesAndroidInjector(modules = [HomeListModule::class])
    abstract fun provideHomeListFragment(): HomeListFragment
}
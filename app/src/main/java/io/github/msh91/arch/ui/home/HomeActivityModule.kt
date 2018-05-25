package io.github.msh91.arch.ui.home

import dagger.Module
import dagger.Provides

@Module
class HomeActivityModule {

    @Provides
    fun provideHomeNavigator(homeActivity: HomeActivity): HomeNavigator {
        return HomeNavigator(homeActivity)
    }
}
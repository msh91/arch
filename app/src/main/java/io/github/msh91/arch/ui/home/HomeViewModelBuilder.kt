package io.github.msh91.arch.ui.home

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.msh91.arch.di.builder.ViewModelKey
import io.github.msh91.arch.ui.home.list.LatestUpdatesViewModel

@Module
abstract class HomeViewModelBuilder {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LatestUpdatesViewModel::class)
    abstract fun bindLatestUpdatesViewModel(latestUpdatesViewModel: LatestUpdatesViewModel): ViewModel
}

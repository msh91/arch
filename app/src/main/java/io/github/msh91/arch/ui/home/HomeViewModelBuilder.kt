package io.github.msh91.arch.ui.home

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.msh91.arch.di.qualifier.viewmodel.ViewModelKey
import io.github.msh91.arch.ui.home.list.HomeListViewModel

@Module
abstract class HomeViewModelBuilder {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeListViewModel::class)
    abstract fun bindHomeListViewModel(homeListViewModel: HomeListViewModel): ViewModel
}
package io.github.msh91.arch.di.builder

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.msh91.arch.di.qualifier.viewmodel.ViewModelKey
import io.github.msh91.arch.ui.home.HomeViewModel
import io.github.msh91.arch.ui.home.list.HomeListViewModel
import io.github.msh91.arch.viewmodel.ArchViewModelFactory

/**
 * With this module all of ViewModels binds into generated Map<Class, ViewModel> and the map
 * will be injected in [ArchViewModelFactory].
 * And finally [ArchViewModelFactory] will be provided as [ViewModelProvider.Factory].
 *
 * Module [RepositoryBuilder] should be installed in this module because some of ViewModels depends
 * of UseCases and Repositories
 */
@Module(includes = [RepositoryBuilder::class])
abstract class ViewModelBuilder {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeListViewModel::class)
    abstract fun bindHomeListViewModel(homeListViewModel: HomeListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(archViewModelFactory: ArchViewModelFactory): ViewModelProvider.Factory
}
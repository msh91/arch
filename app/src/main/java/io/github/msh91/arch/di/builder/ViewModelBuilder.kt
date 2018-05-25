package io.github.msh91.arch.di.builder

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.github.msh91.arch.di.qualifier.viewmodel.ViewModelKey
import io.github.msh91.arch.ui.home.HomeViewModel
import io.github.msh91.arch.viewmodel.ArchViewModelFactory

@Module(/*includes = [ActivityBuilder::class]*/)
abstract class ViewModelBuilder {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(archViewModelFactory: ArchViewModelFactory): ViewModelProvider.Factory
}
package io.github.msh91.arch.di.builder

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import io.github.msh91.arch.ui.home.HomeViewModelBuilder
import io.github.msh91.arch.viewmodel.ArchViewModelFactory

/**
 * With this module all of ViewModels binds into generated Map<Class, ViewModel> and the map
 * will be injected in [ArchViewModelFactory]. In order to do this, we have to bind all
 * ViewModelBuilder modules in this module.
 *
 * And finally [ArchViewModelFactory] will be provided as [ViewModelProvider.Factory].
 *
 */
@Module(
    includes = [
        (HomeViewModelBuilder::class)
    ]
)
abstract class ViewModelBuilder {

    @Binds
    abstract fun bindViewModelFactory(archViewModelFactory: ArchViewModelFactory): ViewModelProvider.Factory
}

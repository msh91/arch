package io.github.msh91.arch.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import io.github.msh91.arch.app.ArchApplication
import io.github.msh91.arch.di.builder.ActivityBuilder
import io.github.msh91.arch.di.module.AppModule
import io.github.msh91.arch.di.module.NetworkModule
import io.github.msh91.arch.di.module.UtilModule
import javax.inject.Singleton

/**
 * Main Application [Component] that included all of modules and sub components.
 */
@Singleton
@Component(
    modules = [
        (NetworkModule::class),
        (AppModule::class),
        (UtilModule::class),
        (AndroidInjectionModule::class),
        (ActivityBuilder::class)
    ]
)

interface AppComponent : AndroidInjector<ArchApplication> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<ArchApplication> {
        interface Factory {
            fun create(@BindsInstance application: Context): AppComponent
        }
    }
}

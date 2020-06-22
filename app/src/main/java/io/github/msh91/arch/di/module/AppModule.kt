package io.github.msh91.arch.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.msh91.arch.app.ArchApplication
import io.github.msh91.arch.di.builder.ViewModelBuilder
import javax.inject.Singleton

/**
 * Main App [Module] that provides default and public classes everywhere
 */
@Module(includes = [ViewModelBuilder::class])
object AppModule {

    /**
     * provides [Application] context as default context.
     */
    @Provides
    @Singleton
    fun provideContext(application: ArchApplication): Context {
        return application
    }
}

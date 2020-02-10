package io.github.msh91.arch.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.msh91.arch.data.mapper.ErrorMapper
import io.github.msh91.arch.di.builder.ViewModelBuilder
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Singleton

/**
 * Main App [Module] that provides default and public classes everywhere
 */
@Module(includes = [ViewModelBuilder::class])
class AppModule {

    /**
     * provides [Application] context as default context.
     */
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    fun provideExceptionHandler(errorMapper: ErrorMapper): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable -> errorMapper.getErrorModel(throwable) }
    }
}

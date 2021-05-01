package io.github.msh91.arch.di.module

import dagger.Binds
import dagger.Module
import io.github.msh91.arch.data.source.local.file.BaseFileProvider
import io.github.msh91.arch.data.source.local.file.FileProvider
import io.github.msh91.arch.util.provider.BaseResourceProvider
import io.github.msh91.arch.util.provider.ResourceProvider

@Module
interface UtilModule {

    /**
     * Provide main implementation of [BaseResourceProvider] to access app raw resources
     */
    @Binds
    fun bindResourceProvider(resourceProvider: ResourceProvider): BaseResourceProvider

    /**
     * Provide main implementation of [BaseFileProvider]
     */
    @Binds
    fun bindFileProvider(fileProvider: FileProvider): BaseFileProvider
}

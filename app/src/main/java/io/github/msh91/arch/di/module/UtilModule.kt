package io.github.msh91.arch.di.module

import dagger.Binds
import dagger.Module
import io.github.msh91.arch.data.source.local.file.BaseFileProvider
import io.github.msh91.arch.data.source.local.file.FileProvider
import io.github.msh91.arch.util.connectivity.BaseConnectionManager
import io.github.msh91.arch.util.connectivity.ConnectionManager
import io.github.msh91.arch.util.device.BaseDeviceUtil
import io.github.msh91.arch.util.device.DeviceUtilImpl
import io.github.msh91.arch.util.providers.BaseResourceProvider
import io.github.msh91.arch.util.providers.ResourceProvider
import javax.inject.Singleton

@Module
interface UtilModule {

    /**
     * provide main implementation of [BaseConnectionManager] to check connection status
     */
    @Binds
    @Singleton
    fun bindConnectionManager(connectionManager: ConnectionManager): BaseConnectionManager

    /**
     * provide main implementation of [BaseDeviceUtil] to access device shared data, unique identifiers, etc
     */
    @Binds
    @Singleton
    fun bindDeviceUtil(deviceUtilImpl: DeviceUtilImpl): BaseDeviceUtil

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

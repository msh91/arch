package io.github.msh91.arch.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.msh91.arch.data.local.AppPreferencesHelper
import io.github.msh91.arch.di.builder.ViewModelBuilder
import io.github.msh91.arch.util.connectivity.BaseConnectionManager
import io.github.msh91.arch.util.connectivity.ConnectionManager
import io.github.msh91.arch.util.device.BaseDeviceUtil
import io.github.msh91.arch.util.device.DeviceUtilImpl
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

    /**
     * provides [AppPreferencesHelper] to access [android.content.SharedPreferences]
     */
    @Provides
    @Singleton
    fun provideAppPreferencesHelper(context: Context): AppPreferencesHelper {
        return AppPreferencesHelper(context)
    }

    /**
     * provides main implementation of [BaseConnectionManager] to check connection status
     */
    @Provides
    @Singleton
    fun provideConnectionManager(context: Context): BaseConnectionManager {
        return ConnectionManager(context)
    }

    /**
     * provides main implementation of [BaseDeviceUtil] to access device shared data, unique identifiers, etc
     */
    @Provides
    @Singleton
    fun provideDeviceUtil(context: Context, appPreferencesHelper: AppPreferencesHelper): BaseDeviceUtil {
        return DeviceUtilImpl(context, appPreferencesHelper)
    }

}

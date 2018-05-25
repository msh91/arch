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

@Module(includes = [ViewModelBuilder::class])
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideAppPreferencesHelper(context: Context): AppPreferencesHelper {
        return AppPreferencesHelper(context)
    }

    @Provides
    @Singleton
    fun provideConnectionManager(context: Context): BaseConnectionManager {
        return ConnectionManager(context)
    }

    @Provides
    @Singleton
    fun provideDeviceUtil(context: Context, appPreferencesHelper: AppPreferencesHelper): BaseDeviceUtil {
        return DeviceUtilImpl(context, appPreferencesHelper)
    }

}

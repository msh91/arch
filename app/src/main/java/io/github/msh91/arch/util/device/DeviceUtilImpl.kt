package io.github.msh91.arch.util.device

import digital.github.msh91.arch.BuildConfig
import io.github.msh91.arch.data.local.AppPreferencesHelper
import java.util.*

class DeviceUtilImpl(
        private val appPreferencesHelper: AppPreferencesHelper) : BaseDeviceUtil {

    override fun getAndroidVersion(): Int {
        return  android.os.Build.VERSION.SDK_INT
    }

    override fun getUniqueId(): String {
        if(appPreferencesHelper.uniqueId.isEmpty())
            appPreferencesHelper.uniqueId = UUID.randomUUID().toString()

        return appPreferencesHelper.uniqueId
    }

    override fun getAppVersion(): Int {
        return BuildConfig.VERSION_CODE
    }
}
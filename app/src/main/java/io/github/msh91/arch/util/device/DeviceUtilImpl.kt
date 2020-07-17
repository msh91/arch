package io.github.msh91.arch.util.device

import io.github.msh91.arch.BuildConfig
import io.github.msh91.arch.data.source.preference.AppPreferencesHelper
import java.util.UUID
import javax.inject.Inject

class DeviceUtilImpl @Inject constructor(
    private val appPreferencesHelper: AppPreferencesHelper
) : BaseDeviceUtil {

    override fun getAndroidVersion(): Int {
        return android.os.Build.VERSION.SDK_INT
    }

    override fun getUniqueId(): String {
        if (appPreferencesHelper.uniqueId.isEmpty()) {
            appPreferencesHelper.uniqueId = UUID.randomUUID().toString()
        }

        return appPreferencesHelper.uniqueId
    }

    override fun getAppVersion(): Int {
        return BuildConfig.VERSION_CODE
    }
}

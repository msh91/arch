package io.github.msh91.arch.util.device

import android.content.Context
import io.github.msh91.arch.data.local.AppPreferencesHelper
import java.util.*

/**
 * Created by m.aghajani on 2/22/2018.
 */
class DeviceUtilImpl(
        val context: Context,
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
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return pInfo.versionCode
    }
}
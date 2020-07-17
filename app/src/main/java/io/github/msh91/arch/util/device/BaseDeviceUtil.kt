package io.github.msh91.arch.util.device

interface BaseDeviceUtil {

    fun getAppVersion(): Int

    fun getAndroidVersion(): Int

//    fun getSimSerialNumber(): String?

    fun getUniqueId(): String
}

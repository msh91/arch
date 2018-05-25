package io.github.msh91.arch.util.connectivity

interface BaseConnectionManager {

    fun isNetworkConnected(): Boolean?

    fun isVPNConnected(): Boolean?

    fun getIPV4(): String?

    fun iSWifi(): Boolean?

    fun isMobileData(): Boolean?
}
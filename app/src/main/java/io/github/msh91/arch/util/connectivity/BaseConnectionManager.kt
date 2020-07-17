package io.github.msh91.arch.util.connectivity

/**
 * An interface to define status of device connectivity
 */
interface BaseConnectionManager {

    /**
     * attempts to check if device is connected to internet or not
     */
    fun isNetworkConnected(): Boolean?

    /**
     * attempts to check if device is connected to a VPN
     */
    fun isVPNConnected(): Boolean?

    /**
     * get IP address that assigned to device
     */
    fun getIPV4(): String?

    /**
     * attempts to check if device is connected to network through a wifi
     */
    fun isWifi(): Boolean?

    /**
     * attempts to check if device is connected to network through Mobile network
     */
    fun isMobileData(): Boolean?
}

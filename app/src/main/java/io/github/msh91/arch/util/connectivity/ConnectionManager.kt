package io.github.msh91.arch.util.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import javax.inject.Inject

/**
 * The main implementation of [BaseConnectionManager], and will be provided through RELEASE mode
 * @see io.github.msh91.arch.di.module.AppModule
 */
class ConnectionManager @Inject constructor(context: Context) : BaseConnectionManager {

    companion object {
        private const val CONNECTION_TIMEOUT = 500
    }

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // TODO change it to valid host for checking internet access
    override fun isNetworkConnected(): Boolean? {
        var result: Boolean? = null
        val nInfo = connectivityManager.activeNetworkInfo
        if (nInfo != null && nInfo.isConnectedOrConnecting) {
            result = InetAddress.getByName("www.google.com").isReachable(CONNECTION_TIMEOUT)
        }
        return result
    }

    override fun isVPNConnected(): Boolean? {
        val nInfo = connectivityManager.activeNetworkInfo
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nInfo != null && nInfo.type == ConnectivityManager.TYPE_VPN
        } else {
            false
        }
    }

    override fun getIPV4(): String? {
        var result: String? = null

        val networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (nInterface in networkInterfaces) {
            val inetAddresses = Collections.list(nInterface.inetAddresses)
            for (address in inetAddresses) {
                if (!address.isLinkLocalAddress) {
                    val hostAddress = address.hostAddress
                    val isIPV4 = hostAddress.indexOf(':') < 0
                    if (isIPV4) {
                        result = hostAddress
                        break
                    }
                }
            }
        }

        return result
    }

    override fun isWifi(): Boolean? {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork?.run { this.type == ConnectivityManager.TYPE_WIFI }
    }

    override fun isMobileData(): Boolean? {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork?.run { this.type == ConnectivityManager.TYPE_MOBILE }
    }
}

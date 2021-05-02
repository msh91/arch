package io.github.msh91.arch.util

import io.github.msh91.arch.BuildConfig
import javax.inject.Inject

/**
 * Helper class to save and get secret data using NDK
 */
class SecretFields @Inject constructor() {

    private val debugCoinMarketBaseUrl = "https://pro-api.coinmarketcap.com/"

    private val releaseCoinMarketBaseUrl = "https://pro-api.coinmarketcap.com/"

    val apiKey: String = "d9714d48-05fb-494e-aa2c-e1113c178385"

    private val debugChartBaseUrl = "https://api.blockchain.info/"

    private val releaseChartBaseUrl = "https://api.blockchain.info/"

    fun getCoinMarketBaseUrl(): String {
        return if (BuildConfig.DEBUG) {
            debugCoinMarketBaseUrl
        } else {
            releaseCoinMarketBaseUrl
        }
    }

    fun getChartBaseUrl(): String {
        return if (BuildConfig.DEBUG) {
            debugChartBaseUrl
        } else {
            releaseChartBaseUrl
        }
    }
}

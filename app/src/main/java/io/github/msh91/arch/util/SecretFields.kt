package io.github.msh91.arch.util

import io.github.msh91.arch.BuildConfig
import javax.inject.Inject

/**
 * Helper class to save and get secret data using NDK
 */
class SecretFields @Inject constructor() {

    private val debugBaseUrl = "https://pro-api.coinmarketcap.com/"

    private val releaseBaseUrl = "https://pro-api.coinmarketcap.com/"

    val apiKey: String = "d9714d48-05fb-494e-aa2c-e1113c178385"

    fun getBaseUrl(): String {
        return if (BuildConfig.DEBUG)
            debugBaseUrl
        else
            releaseBaseUrl
    }

}
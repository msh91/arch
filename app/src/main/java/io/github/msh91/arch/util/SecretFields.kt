package io.github.msh91.arch.util

import io.github.msh91.arch.BuildConfig

/**
 * Helper class to save and get secret data using NDK
 */
class SecretFields {

    private fun debugBaseURI(): String {
        return "http://io.github.msh91"
    }

    private fun releaseBaseURI(): String {
        return "http://io.github.msh91"
    }

    fun authorizationKey(): String {
        return "authorization"
    }

    fun getBaseURI(): String {
        if (BuildConfig.BUILD_TYPE == "debug")
            return debugBaseURI()
        else if (BuildConfig.BUILD_TYPE == "release")
            return releaseBaseURI()
        else
            return ""
    }

}
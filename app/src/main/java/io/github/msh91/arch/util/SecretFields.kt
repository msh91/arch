package io.github.msh91.arch.util

import io.github.msh91.arch.BuildConfig

/**
 * Helper class to save and get secret data using NDK
 */
class SecretFields
{
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    private external fun debugBaseURI(): String

    private external fun releaseBaseURI(): String

    external fun authorizationKey(): String

    fun getBaseURI(): String{
        if(BuildConfig.BUILD_TYPE == "debug")
            return debugBaseURI()
        else if(BuildConfig.BUILD_TYPE == "release")
            return releaseBaseURI()
        else
            return ""
    }

}
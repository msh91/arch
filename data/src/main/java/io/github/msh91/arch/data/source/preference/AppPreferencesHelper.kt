package io.github.msh91.arch.data.source.preference

import android.content.Context
import javax.inject.Inject

/**
 * With this helper we can access all shared preferences.
 * Every field uses [PreferenceDelegate] for managing get and set value
 */
class AppPreferencesHelper @Inject constructor(context: Context) {

    val prefs by lazy { context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) }

    companion object {
        private const val PREF_NAME = "pref_arch"
        private const val ACCESS_TOKEN = "UserToken"
        private const val TOKEN_TYPE = "TokenType"
        private const val REFRESH_TOKEN = "RefreshToken"
        private const val UNIQUE_ID = "UniqueId"
    }

    /**
     * provide saving and getting access token from preferences in order to use in with-token api services
     */
    var token: String by PreferenceDelegate(prefs, ACCESS_TOKEN, "")

    /**
     * provide saving and getting token type from preferences in order to use in with-token api services
     */
    var tokenType: String by PreferenceDelegate(prefs, TOKEN_TYPE, "")

    /**
     * provide saving and getting refresh token from preferences in order to use in [TokenAuthenticator]
     */
    var refreshToken: String by PreferenceDelegate(prefs, REFRESH_TOKEN, "")

    /**
     * provide saving and getting device unique id from preferences in order to use in [DeviceUtil]
     */
    var uniqueId: String by PreferenceDelegate(prefs, UNIQUE_ID, "")
}

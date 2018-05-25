package io.github.msh91.arch.data.local

import android.content.Context

class AppPreferencesHelper(context: Context) {

    val prefs by lazy { context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) }

    companion object {
        private const val PREF_NAME = "pref_arch"
        private const val ACCESS_TOKEN = "UserToken"
        private const val TOKEN_TYPE = "TokenType"
        private const val REFRESH_TOKEN = "RefreshToken"
        private const val UNIQUE_ID = "UniqueId"
    }

    var token: String by PreferenceDelegate(prefs, ACCESS_TOKEN, "")

    var tokenType: String by PreferenceDelegate(prefs, TOKEN_TYPE, "")

    var refreshToken: String by PreferenceDelegate(prefs, REFRESH_TOKEN, "")

    var uniqueId: String by PreferenceDelegate(prefs, UNIQUE_ID, "")
}

package io.github.msh91.arcyto.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import javax.inject.Inject

private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)

class UserPreferencesDataStore @Inject constructor(context: Context) : PreferencesDataStore {
    override val dataStore: DataStore<Preferences> = context.userPreferencesDataStore
}


package io.github.msh91.arcyto.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PreferencesDataStore {
    val dataStore: DataStore<Preferences>

    fun getStringsSet(key: Preferences.Key<Set<String>>): Flow<Set<String>> {
        return dataStore.data.map { it[key] ?: emptySet() }
    }

    suspend fun updateStringsSet(key: Preferences.Key<Set<String>>, set: Set<String>) {
        dataStore.edit { it[key] = set }
    }
}
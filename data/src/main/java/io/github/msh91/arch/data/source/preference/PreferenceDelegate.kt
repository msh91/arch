package io.github.msh91.arch.data.source.preference

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferenceDelegate<T>(
    private val prefs: SharedPreferences,
    val name: String,
    private val default: T
) : ReadWriteProperty<Any?, T> {
    private var cachedT: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = cachedT
        ?: findPreference(name, default)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    @Suppress("UNCHECKED_CAST")
    private fun findPreference(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default).orEmpty()
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can not be saved into preferences")
        }
        (res as T).also { cachedT = it }
    }

    private fun putPreference(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can not be saved into preferences")
        }.apply()
        cachedT = null
    }
}

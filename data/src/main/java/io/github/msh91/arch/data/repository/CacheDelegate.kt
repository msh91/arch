package io.github.msh91.arch.data.repository

import android.util.Log
import java.util.Date
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Delegation for the caching APIs data.
 *
 * @param expirationTimeMillis set expiration time in milliseconds if you need to have a time expiration factor. Default value is 0L
 * @param predicateExpired a predicate lambda function in order to specify if data is expired or not. Default value is null
 */
class CacheDelegate<T>(
    private val expirationTimeMillis: Long = 0,
    private val predicateExpired: ((T) -> Boolean)? = null
) : ReadWriteProperty<Any?, T?> {
    companion object {
        const val CACHE_FOR_5_MINUTES: Long = 5 * 60 * 1000
        const val CACHE_FOR_10_MINUTES: Long = 10 * 60 * 1000
        const val CACHE_FOR_20_MINUTES: Long = 20 * 60 * 1000
    }

    private var cached: T? = null
    private var lastSavedTime: Long = 0

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return (if (dataIsCachedAndNotExpired()) cached else null).also {
            Log.d("delegate", "getValue: cached: $it")
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        cached = value
        lastSavedTime = Date().time
    }

    /**
     * Checks if an element (Specific T) exists in the cache and not expired yet.
     */
    private fun dataIsCachedAndNotExpired(): Boolean = cached != null && !isExpired() && !timeExpired()

    /**
     * check to ensure if data is expired or not, based on [predicateExpired] action
     */
    private fun isExpired(): Boolean = (predicateExpired?.invoke(cached!!) ?: false)

    /**
     * check to ensure if data is expired or not, based on expiration time
     */
    private fun timeExpired(): Boolean = if (expirationTimeMillis != 0L) {
        (Date().time - lastSavedTime) > expirationTimeMillis
    } else {
        false
    }
}

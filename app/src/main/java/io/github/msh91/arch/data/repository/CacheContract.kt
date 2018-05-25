package io.github.msh91.arch.data.repository

import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the caching of APIs data. This is to be implemented by the
 * cache layer, using this interface as a way of communicating.
 */
interface CacheContract<T> {

    var model: T?

    /**
     * Retrieve a list of data, from the cache
     */
    fun data(): Single<T>? = if (model != null && isCached() && !isExpired) Single.just(model) else null

    /**
     * Checks if an element (Specific T) exists in the cache.
     */
    fun isCached(): Boolean = model != null

    /**
     * Checks if the cache is expired.
     *
     * @return true, the cache is expired, otherwise false.
     */
    val isExpired: Boolean

    /**
     * Clear all data from the cache
     */
    fun clearData(): Completable {
        model = null
        return Completable.complete()
    }

    /**
     * Save a given list of data to the cache
     */
    fun saveData(data: T): Completable {
        model = data

        return Completable.complete()
    }

    /**
     */
    fun setLastCacheTime(lastCache: Long?)
}

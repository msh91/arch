package io.github.msh91.arch.data.mapper

import io.github.msh91.arch.data.model.Error
import javax.inject.Inject

/**
 * A util class that generate an instance of [Error] with happened [Throwable]
 */
class ErrorMapper @Inject constructor(private val httpErrorMapper: HttpErrorMapper) {

    /**
     * Generate an instance of [Error] from happened [Throwable]
     * @param t Raised [Throwable]
     *
     * @return returns an instance of [Error]
     */
    fun getError(t: Throwable): Error {
        // if connection was successful but no data received
        if (t is NullPointerException) {
            return Error.Null
        }
        val httpError = httpErrorMapper.mapToErrorModel(t)
        if (httpError != null) {
            return httpError
        }
        // something happened that we did not make our self ready for it
        return Error.NotDefined(t)
    }
}

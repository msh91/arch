package io.github.msh91.arch.data.mapper

import io.github.msh91.arch.data.model.response.ErrorModel
import io.github.msh91.arch.data.model.response.ErrorStatus
import javax.inject.Inject

/**
 * A util class that generate an instance of [ErrorModel] with happened [Throwable]
 */
class ErrorMapper @Inject constructor(private val cloudErrorMapper: CloudErrorMapper) {

    /**
     * Generate an instance of [ErrorModel] with happened [Throwable]
     * @param t happened [Throwable]
     *
     * @return rentuns an instance of [ErrorModel]
     */
    fun getErrorModel(t: Throwable): ErrorModel {
        // if response was successful but no data received
        if (t is NullPointerException) {
            return ErrorModel(ErrorStatus.EMPTY_RESPONSE)
        }
        val cloudError = cloudErrorMapper.mapToErrorModel(t)
        if (cloudError != null) {
            return cloudError
        }
        // something happened that we did not make our self ready for it
        return ErrorModel(ErrorStatus.NOT_DEFINED)
    }
}


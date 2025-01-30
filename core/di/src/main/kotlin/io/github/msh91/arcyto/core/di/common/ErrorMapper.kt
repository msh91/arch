package io.github.msh91.arcyto.core.di.common

import javax.inject.Inject

interface ErrorMapper {
    /**
     * returns a localized error message or null if the throwable is not supported by the error mapper
     */
    fun getErrorMessage(exception: Throwable): String?
}


class CompositeErrorMapper @Inject constructor(
    private val mappers: Set<@JvmSuppressWildcards ErrorMapper>,
) {
    fun getErrorMessage(exception: Throwable): String {
        return mappers
            .firstNotNullOfOrNull { it.getErrorMessage(exception) }
            ?: exception.localizedMessage.orEmpty()
    }
}

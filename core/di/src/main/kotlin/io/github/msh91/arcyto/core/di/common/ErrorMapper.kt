package io.github.msh91.arcyto.core.di.common

import dev.zacsweers.metro.Inject

interface ErrorMapper {
    /**
     * returns a localized error message or null if the throwable is not supported by the error mapper
     */
    fun getErrorMessage(exception: Throwable): String?
}

@Inject
class CompositeErrorMapper(
    private val mappers: Set<@JvmSuppressWildcards ErrorMapper>,
) {
    fun getErrorMessage(exception: Throwable): String =
        mappers
            .firstNotNullOfOrNull { it.getErrorMessage(exception) }
            ?: exception.localizedMessage.orEmpty()
}

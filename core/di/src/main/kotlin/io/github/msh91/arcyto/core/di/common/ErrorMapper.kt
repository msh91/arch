package io.github.msh91.arcyto.core.di.common

import android.content.Context
import io.github.msh91.arcyto.core.di.R
import javax.inject.Inject

interface ErrorMapper {
    /**
     * returns a localized error message or null if the throwable is not supported by the error mapper
     */
    fun getErrorMessage(exception: Throwable): String?
}


class CompositeErrorMapper @Inject constructor(
    private val context: Context,
    private val mappers: Set<@JvmSuppressWildcards ErrorMapper>,
) {
    fun getErrorMessage(exception: Throwable): String {
        return mappers
            .firstNotNullOfOrNull { it.getErrorMessage(exception) }
            ?: exception.localizedMessage
            ?: context.resources.getString(R.string.error_unknown)
    }
}

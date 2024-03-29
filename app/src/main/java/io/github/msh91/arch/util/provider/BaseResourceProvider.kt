package io.github.msh91.arch.util.provider

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import io.github.msh91.arch.data.model.Error

/**
 * Resolves application's resources.
 */
interface BaseResourceProvider {

    /**
     * Resolves text's id to String.
     *
     * @param id to be fetched from the resources
     * @return String representation of the {@param id}
     */
    fun getString(@StringRes id: Int): String

    /**
     * Resolves text's id to String and formats it.
     *
     * @param resId      to be fetched from the resources
     * @param formatArgs format arguments
     * @return String representation of the {@param resId}
     */
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String

    /**
     * get error message of the occurred error
     */
    fun getErrorMessage(error: Error): String

    /**
     * Resolves color's id to int
     *
     * @param resId to ne fetched from the resources
     * @return Int representation of the {@param id}
     */
    fun getColor(@ColorRes resId: Int): Int
}

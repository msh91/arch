package io.github.msh91.arch.data.model.response

/**
 * base sealed class for handling result from data layer
 */
sealed class Result<out T>

/**
 * Wrapper for success response of repository calls
 */
data class Success<out T>(val value: T): Result<T>()

/**
 * Wrapper for error response of repository calls
 */
data class Error<out T>(val error: ErrorModel): Result<T>()
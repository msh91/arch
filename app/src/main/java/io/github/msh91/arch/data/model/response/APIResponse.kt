package io.github.msh91.arch.data.model.response

import io.github.msh91.arch.data.model.response.error.ErrorModel
import io.github.msh91.arch.domain.BaseUseCase
/**
 * base sealed class for handling api responses in [BaseUseCase]
 * @see [BaseUseCase]
 */
sealed class APIResponse<out T>

/**
 * Wrapper for success response of api calls
 */
data class SuccessResponse<out T>(val value: T): APIResponse<T>()

/**
 * Wrapper for error response of api calls
 */
data class ErrorResponse<out T>(val error: ErrorModel): APIResponse<T>()
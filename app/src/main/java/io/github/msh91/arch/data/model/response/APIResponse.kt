package io.github.msh91.arch.data.model.response

import io.github.msh91.arch.data.model.response.error.ErrorModel

sealed class APIResponse<out T>
data class SuccessResponse<out T>(val value: T): APIResponse<T>()
data class ErrorResponse<out T>(val error: ErrorModel): APIResponse<T>()
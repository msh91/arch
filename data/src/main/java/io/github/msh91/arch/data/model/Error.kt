package io.github.msh91.arch.data.model

sealed class Error {
    /**
     * an unexpected error
     */
    data class NotDefined(val throwable: Throwable) : Error()

    object Null : Error()
}

sealed class HttpError : Error() {

    object ConnectionFailed : HttpError()

    data class InvalidResponse(val code: Int, val message: String?) : HttpError()

    object TimeOut : HttpError()

    object UnAuthorized : HttpError()
}

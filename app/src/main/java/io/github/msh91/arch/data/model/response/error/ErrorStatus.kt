package io.github.msh91.arch.data.model.response.error

/**
 * various error status to know what happened if something goes wrong with an api call
 */
enum class ErrorStatus {
    /**
     * error in connecting to repository (Server or Database)
     */
    NO_CONNECTION,
    /**
     * error in getting value (Json Error, Server Error, etc)
     */
    BAD_RESPONSE,
    /**
     * Time out  error
     */
    TIMEOUT,
    /**
     * no data available in repository
     */
    EMPTY_RESPONSE,
    /**
     * an unexpected error
     */
    NOT_DEFINED,
    /**
     * bad credential
     */
    UNAUTHORIZED
}
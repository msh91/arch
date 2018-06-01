package io.github.msh91.arch.data.model.response.error

import com.google.gson.annotations.SerializedName

/**
 * Default error model that comes from server if something goes wrong with an api call
 */
data class ErrorModel(
        @SerializedName("path") val path: String?,
        @SerializedName("message") val message: String?,
        @SerializedName("code") val code: Int?,
        @Transient var errorStatus: ErrorStatus
)
{
    constructor(errorStatus: ErrorStatus) : this(null, null, null, errorStatus)
}
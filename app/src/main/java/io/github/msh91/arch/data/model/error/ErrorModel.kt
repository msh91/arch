package io.github.msh91.arch.data.model.error

import com.google.gson.annotations.SerializedName


data class ErrorModel(
        @SerializedName("path") val path: String?,
        @SerializedName("message") val message: String?,
        @SerializedName("code") val code: Int?,
        @Transient var errorStatus: ErrorStatus
)
{
    constructor(errorStatus: ErrorStatus) : this(null, null, null, errorStatus)
}
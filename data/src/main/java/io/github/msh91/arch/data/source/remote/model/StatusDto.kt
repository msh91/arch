package io.github.msh91.arch.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class StatusDto(
    @SerializedName("timestamp") val generatedTime: String,
    @SerializedName("error_code") val errorCode: Int,
    @SerializedName("error_message") val errorMessage: Int
)

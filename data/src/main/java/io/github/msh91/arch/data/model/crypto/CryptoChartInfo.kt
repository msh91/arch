package io.github.msh91.arch.data.model.crypto

import com.google.gson.annotations.SerializedName

data class CryptoChartInfo(
    @SerializedName("name") val name: String,
    @SerializedName("unit") val unit: String,
    @SerializedName("description") val description: String,
    @SerializedName("values") val chartValues: List<ChartValue>
)

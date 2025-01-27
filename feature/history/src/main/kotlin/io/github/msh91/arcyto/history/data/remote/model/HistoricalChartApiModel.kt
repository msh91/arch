package io.github.msh91.arcyto.history.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoricalChartApiModel(
    @SerialName("prices") val prices: List<List<Double>>,
    @SerialName("market_caps") val marketCaps: List<List<Double>>,
    @SerialName("total_volumes") val totalVolumes: List<List<Double>>,
)
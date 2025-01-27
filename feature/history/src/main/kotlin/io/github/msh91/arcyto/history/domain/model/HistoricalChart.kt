package io.github.msh91.arcyto.history.domain.model

data class HistoricalChart(
    val prices: List<HistoricalValue>,
    val marketCaps: List<HistoricalValue>,
    val totalVolumes: List<HistoricalValue>,
)

data class HistoricalValue(
    val date: Long,
    val value: Double,
)
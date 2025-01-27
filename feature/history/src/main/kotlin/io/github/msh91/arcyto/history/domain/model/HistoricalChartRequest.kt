package io.github.msh91.arcyto.history.domain.model

data class HistoricalChartRequest(
    val id: String,
    val currency: String,
    val days: Int,
    val interval: String,
    val precision: Int,
)

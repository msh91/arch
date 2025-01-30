package io.github.msh91.arcyto.history.domain.model

data class HistoricalChart(
    val chartPrices: List<ChartPrice>,
)

data class ChartPrice(
    val date: Long,
    val value: Double,
)
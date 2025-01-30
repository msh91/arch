package io.github.msh91.arcyto.history.data.mapper

import io.github.msh91.arcyto.history.data.remote.model.HistoricalChartApiModel
import io.github.msh91.arcyto.history.domain.model.HistoricalChart
import io.github.msh91.arcyto.history.domain.model.ChartPrice


fun HistoricalChartApiModel.toDomain() = HistoricalChart(
    chartPrices = prices.map { ChartPrice(it[0].toLong(), it[1]) },
)
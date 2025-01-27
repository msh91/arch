package io.github.msh91.arcyto.history.data.mapper

import io.github.msh91.arcyto.history.data.remote.model.HistoricalChartApiModel
import io.github.msh91.arcyto.history.domain.model.HistoricalChart
import io.github.msh91.arcyto.history.domain.model.HistoricalValue


fun HistoricalChartApiModel.toDomain() = HistoricalChart(
    prices = prices.map { HistoricalValue(it[0].toLong(), it[1]) },
    marketCaps = marketCaps.map { HistoricalValue(it[0].toLong(), it[1]) },
    totalVolumes = totalVolumes.map { HistoricalValue(it[0].toLong(), it[1]) },
)
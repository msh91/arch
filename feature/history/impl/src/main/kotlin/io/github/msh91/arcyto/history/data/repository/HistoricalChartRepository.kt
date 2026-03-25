package io.github.msh91.arcyto.history.data.repository

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.msh91.arcyto.history.data.mapper.toDomain
import io.github.msh91.arcyto.history.data.remote.HistoricalRemoteDataSource
import io.github.msh91.arcyto.history.domain.model.HistoricalChart
import io.github.msh91.arcyto.history.domain.model.HistoricalChartRequest
import io.github.msh91.arcyto.history.domain.model.LatestPrice

interface HistoricalChartRepository {
    suspend fun getHistoricalChart(request: HistoricalChartRequest): Result<HistoricalChart>

    suspend fun getLatestCoinPrice(
        coinId: String,
        currency: String,
        precision: Int,
    ): Result<LatestPrice>
}

/**
 * HistoricalChartRepository implementation that uses the [HistoricalRemoteDataSource] to fetch the historical chart.
 */
@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class HistoricalChartRepositoryImpl(
    private val historicalRemoteDataSource: HistoricalRemoteDataSource,
) : HistoricalChartRepository {
    override suspend fun getHistoricalChart(request: HistoricalChartRequest): Result<HistoricalChart> =
        historicalRemoteDataSource
            .getHistoricalChart(request.id, request.currency, request.days, request.interval, request.precision)
            .map { it.toDomain() }

    override suspend fun getLatestCoinPrice(
        coinId: String,
        currency: String,
        precision: Int,
    ): Result<LatestPrice> =
        historicalRemoteDataSource
            .getLatestCoinPrice(coinId, currency, precision)
            .map { result ->
                val priceApiModel = result.getValue(coinId)
                LatestPrice(
                    coinId = coinId,
                    price = priceApiModel.priceInEur,
                    lastUpdated = priceApiModel.lastUpdatedAt,
                    changePercentage = priceApiModel.changePercentage,
                )
            }
}

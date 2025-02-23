package io.github.msh91.arcyto.history.data.repository

import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.history.data.mapper.toDomain
import io.github.msh91.arcyto.history.data.remote.HistoricalRemoteDataSource
import io.github.msh91.arcyto.history.domain.model.HistoricalChart
import io.github.msh91.arcyto.history.domain.model.HistoricalChartRequest
import io.github.msh91.arcyto.history.domain.model.LatestPrice
import javax.inject.Inject
import javax.inject.Singleton

interface HistoricalChartRepository {
    suspend fun getHistoricalChart(request: HistoricalChartRequest): Result<HistoricalChart>

    suspend fun getLatestCoinPrice(coinId: String, currency: String, precision: Int): Result<LatestPrice>
}

/**
 * HistoricalChartRepository implementation that uses the [HistoricalRemoteDataSource] to fetch the historical chart.
 */
@ContributesBinding(AppScope::class)
@Singleton
class HistoricalChartRepositoryImpl @Inject constructor(
    private val historicalRemoteDataSource: HistoricalRemoteDataSource,
) : HistoricalChartRepository {

    override suspend fun getHistoricalChart(request: HistoricalChartRequest): Result<HistoricalChart> =
        historicalRemoteDataSource
            .getHistoricalChart(request.id, request.currency, request.days, request.interval, request.precision)
            .map { it.toDomain() }

    override suspend fun getLatestCoinPrice(coinId: String, currency: String, precision: Int): Result<LatestPrice> =
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
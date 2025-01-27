package io.github.msh91.arcyto.history.data.repository

import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.history.data.mapper.toDomain
import io.github.msh91.arcyto.history.data.remote.HistoricalRemoteDataSource
import io.github.msh91.arcyto.history.domain.model.HistoricalChart
import io.github.msh91.arcyto.history.domain.model.HistoricalChartRequest
import javax.inject.Inject
import javax.inject.Singleton

interface HistoricalChartRepository {
    suspend fun getHistoricalChart(request: HistoricalChartRequest): Result<HistoricalChart>
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
}
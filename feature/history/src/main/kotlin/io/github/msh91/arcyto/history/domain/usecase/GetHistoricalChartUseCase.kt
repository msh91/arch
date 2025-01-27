package io.github.msh91.arcyto.history.domain.usecase

import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.history.data.repository.HistoricalChartRepository
import io.github.msh91.arcyto.history.domain.model.HistoricalChart
import io.github.msh91.arcyto.history.domain.model.HistoricalChartRequest
import io.github.msh91.arcyto.history.domain.model.HistoricalPrice
import javax.inject.Inject

interface GetHistoricalChartUseCase {
    suspend operator fun invoke(request: HistoricalChartRequest): Result<List<HistoricalPrice>>
}

@ContributesBinding(AppScope::class)
class GetHistoricalChartUseCaseImpl @Inject constructor(
    private val historicalChartRepository: HistoricalChartRepository,
) : GetHistoricalChartUseCase {

    override suspend fun invoke(request: HistoricalChartRequest): Result<List<HistoricalPrice>> =
        historicalChartRepository.getHistoricalChart(request)
            .map { it.toHistoricalPrices() }

    private fun HistoricalChart.toHistoricalPrices(): List<HistoricalPrice> = prices
        .sortedByDescending { it.date }
        .mapIndexed { index, historicalValue ->
            val value = historicalValue.value
            val previousValue = if (index == prices.lastIndex) null else prices[index + 1].value
            val changePercentage = previousValue?.let { (value - previousValue) / previousValue * 100 }
            HistoricalPrice(
                date = historicalValue.date,
                value = historicalValue.value,
                // diff percentage with 2 decimals
                changePercentage = changePercentage,
            )
        }
}
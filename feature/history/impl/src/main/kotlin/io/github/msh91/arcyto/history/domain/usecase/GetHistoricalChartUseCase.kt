package io.github.msh91.arcyto.history.domain.usecase

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.msh91.arcyto.history.data.repository.HistoricalChartRepository
import io.github.msh91.arcyto.history.domain.model.HistoricalChart
import io.github.msh91.arcyto.history.domain.model.HistoricalChartRequest
import io.github.msh91.arcyto.history.domain.model.HistoricalPrice

interface GetHistoricalChartUseCase {
    suspend operator fun invoke(request: HistoricalChartRequest): Result<List<HistoricalPrice>>
}

@Inject
@ContributesBinding(AppScope::class)
class GetHistoricalChartUseCaseImpl(
    private val historicalChartRepository: HistoricalChartRepository,
) : GetHistoricalChartUseCase {
    override suspend fun invoke(request: HistoricalChartRequest): Result<List<HistoricalPrice>> =
        historicalChartRepository
            .getHistoricalChart(request)
            .map { it.toHistoricalPrices() }

    private fun HistoricalChart.toHistoricalPrices(): List<HistoricalPrice> {
        val sortedPrices = chartPrices.sortedByDescending { it.date }
        return sortedPrices.mapIndexed { index, historicalValue ->
            val value = historicalValue.value
            val previousValue = if (index == sortedPrices.lastIndex) null else sortedPrices[index + 1].value
            val changePercentage = previousValue?.let { (value - previousValue) / previousValue * 100 }

            HistoricalPrice(
                date = historicalValue.date,
                value = historicalValue.value,
                // diff percentage is null if there is no previous value
                changePercentage = changePercentage,
            )
        }
    }
}

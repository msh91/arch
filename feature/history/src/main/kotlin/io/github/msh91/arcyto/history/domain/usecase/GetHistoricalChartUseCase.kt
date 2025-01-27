package io.github.msh91.arcyto.history.domain.usecase

import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.history.data.repository.HistoricalChartRepository
import io.github.msh91.arcyto.history.domain.model.HistoricalChart
import io.github.msh91.arcyto.history.domain.model.HistoricalChartRequest
import javax.inject.Inject

interface GetHistoricalChartUseCase {
    suspend operator fun invoke(request: HistoricalChartRequest): Result<HistoricalChart>
}

@ContributesBinding(AppScope::class)
class GetHistoricalChartUseCaseImpl @Inject constructor(
    private val historicalChartRepository: HistoricalChartRepository,
) : GetHistoricalChartUseCase {

    override suspend fun invoke(request: HistoricalChartRequest): Result<HistoricalChart> =
        historicalChartRepository.getHistoricalChart(request)
}
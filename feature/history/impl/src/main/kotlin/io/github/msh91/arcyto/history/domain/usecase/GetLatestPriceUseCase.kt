package io.github.msh91.arcyto.history.domain.usecase

import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.history.data.repository.HistoricalChartRepository
import io.github.msh91.arcyto.history.domain.model.LatestPrice
import io.github.msh91.arcyto.history.domain.model.LatestPriceRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

interface GetLatestPriceUseCase {
    suspend operator fun invoke(request: LatestPriceRequest): Flow<Result<LatestPrice>>
}

/**
 * This implementation tries to fetch the latest price every 60 seconds and publish the result via a shared flow
 */
@ContributesBinding(AppScope::class)
class GetLatestPriceUseCaseImpl @Inject constructor(
    private val historicalChartRepository: HistoricalChartRepository,
) : GetLatestPriceUseCase {

    override suspend fun invoke(request: LatestPriceRequest): Flow<Result<LatestPrice>> {
        // Fetch the latest price in an interval provided by the request
        val coroutineScope = CoroutineScope(currentCoroutineContext() + SupervisorJob())
        return flow {
            while (true) {
                emit(
                    historicalChartRepository.getLatestCoinPrice(
                        coinId = request.coinId,
                        currency = request.currency,
                        precision = request.precision
                    )
                )
                delay(request.intervalMs)
            }
        }.shareIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )
    }
}
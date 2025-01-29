package io.github.msh91.arcyto.history.domain.usecase

import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.history.data.repository.HistoricalChartRepository
import io.github.msh91.arcyto.history.domain.model.LatestPrice
import io.github.msh91.arcyto.history.domain.model.LatestPriceRequest
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface GetLatestPriceUseCase {
    operator fun invoke(request: LatestPriceRequest): Flow<Result<LatestPrice>>
}

/**
 * This implementation tries to fetch the latest price every 60 seconds and publish the result via a shared flow
 */
@ContributesBinding(AppScope::class)
class GetLatestPriceUseCaseImpl @Inject constructor(
    private val historicalChartRepository: HistoricalChartRepository,
) : GetLatestPriceUseCase {
    private val latestPriceFlow = MutableSharedFlow<Result<LatestPrice>>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    override fun invoke(request: LatestPriceRequest): Flow<Result<LatestPrice>> {
        // Fetch the latest price every 60 seconds
        request.coroutineScope.launch {
            //repeat every 60 seconds
            while (true) {
                latestPriceFlow.emit(historicalChartRepository.getLatestCoinPrice(request))
                delay(request.intervalMs)
            }
        }
        return latestPriceFlow.asSharedFlow()
    }
}
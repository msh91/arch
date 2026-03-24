package io.github.msh91.arcyto.details.data.repository

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.msh91.arcyto.details.data.mapper.toDomainModel
import io.github.msh91.arcyto.details.data.remote.CoinDetailsDataSource
import io.github.msh91.arcyto.details.domain.model.CoinDetails
import io.github.msh91.arcyto.details.domain.model.CoinDetailsRequest

interface CoinDetailsRepository {
    suspend fun getCoinDetails(request: CoinDetailsRequest): Result<CoinDetails>
}

@Inject
@ContributesBinding(AppScope::class)
class CoinDetailsRepositoryImpl(
    private val coinDetailsDataSource: CoinDetailsDataSource,
) : CoinDetailsRepository {
    override suspend fun getCoinDetails(request: CoinDetailsRequest): Result<CoinDetails> =
        coinDetailsDataSource
            .getCoinDetails(request.id, request.date, request.localization)
            .map { it.toDomainModel() }
}

package io.github.msh91.arcyto.details.data.repository

import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.details.data.mapper.toDomainModel
import io.github.msh91.arcyto.details.data.remote.CoinDetailsDataSource
import io.github.msh91.arcyto.details.domain.model.CoinDetails
import io.github.msh91.arcyto.details.domain.model.CoinDetailsRequest
import javax.inject.Inject

interface CoinDetailsRepository {
    suspend fun getCoinDetails(request: CoinDetailsRequest): Result<CoinDetails>
}

@ContributesBinding(AppScope::class)
class CoinDetailsRepositoryImpl @Inject constructor(
    private val coinDetailsDataSource: CoinDetailsDataSource,
) : CoinDetailsRepository {

    override suspend fun getCoinDetails(request: CoinDetailsRequest): Result<CoinDetails> =
        coinDetailsDataSource
            .getCoinDetails(request.id, request.date, request.localization)
            .map { it.toDomainModel() }
}
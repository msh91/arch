package io.github.msh91.arch.data.repository.crypto

import arrow.core.Either
import io.github.msh91.arch.data.di.qualifier.Concrete
import io.github.msh91.arch.data.mapper.ErrorMapper
import io.github.msh91.arch.data.model.Error
import io.github.msh91.arch.data.model.crypto.CryptoChartInfo
import io.github.msh91.arch.data.repository.BaseRepository
import io.github.msh91.arch.data.source.remote.ChartDataSource
import javax.inject.Inject

class CryptoChartRepository @Inject constructor(
    errorMapper: ErrorMapper,
    private val chartDataSource: ChartDataSource
) : BaseRepository(errorMapper) {

    suspend fun getChartInfo(): Either<Error, CryptoChartInfo> {
        return safeApiCall { chartDataSource.getCryptoChartInfo() }
    }
}

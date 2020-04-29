package io.github.msh91.arch.data.repository.crypto

import arrow.core.Either
import io.github.msh91.arch.data.di.qualifier.Concrete
import io.github.msh91.arch.data.mapper.ErrorMapper
import io.github.msh91.arch.data.model.Error
import io.github.msh91.arch.data.model.crypto.CryptoCurrency
import io.github.msh91.arch.data.repository.BaseRepository
import io.github.msh91.arch.data.source.remote.CryptoDataSource
import javax.inject.Inject

class CryptoRepository @Inject constructor(
        errorMapper: ErrorMapper,
        @Concrete private val cryptoDataSource: CryptoDataSource
) : BaseRepository(errorMapper) {

    suspend fun getLatestUpdates(
            start: Int = 1,
            limit: Int = 20,
            convertTo: String = "USD"
    ): Either<Error, List<CryptoCurrency>> {

        return safeApiCall { cryptoDataSource.getLatestUpdates(start, limit, convertTo) }
                .map { it.data }

    }
}
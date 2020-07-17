package io.github.msh91.arch.data.source.remote

import io.github.msh91.arch.data.model.crypto.CryptoCurrency
import io.github.msh91.arch.data.source.remote.model.ResponseWrapperDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoDataSource {
    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getLatestUpdates(
        @Query("start") start: Int,
        @Query("limit") limit: Int,
        @Query("convert") convertTo: String
    ): ResponseWrapperDto<List<CryptoCurrency>>
}

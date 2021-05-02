package io.github.msh91.arch.data.source.remote

import io.github.msh91.arch.data.model.crypto.CryptoChartInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface ChartDataSource {
    @GET("charts/market-price")
    suspend fun getCryptoChartInfo(
        @Query("timespan") timeSpan: String = "60days",
        @Query("sampled") sampled: Boolean = true,
        @Query("format") format: String = "json"
    ): CryptoChartInfo
}

package io.github.msh91.arcyto.history.data.remote

import io.github.msh91.arcyto.history.data.remote.model.CoinsLatestPricesApiModel
import io.github.msh91.arcyto.history.data.remote.model.HistoricalChartApiModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoricalRemoteDataSource {

    @GET("coins/{id}/market_chart")
    suspend fun getHistoricalChart(
        @Path("id") id: String,
        @Query("vs_currency") currency: String,
        @Query("days") days: Int,
        @Query("interval") interval: String,
        @Query("precision") precision: Int,
    ): Result<HistoricalChartApiModel>

    @GET("simple/price?include_24hr_change=true&include_last_updated_at=true")
    suspend fun getLatestCoinPrice(
        @Query("ids") ids: String,
        @Query("vs_currencies") currency: String,
        @Query("precision") precision: Int,
    ): Result<CoinsLatestPricesApiModel>
}
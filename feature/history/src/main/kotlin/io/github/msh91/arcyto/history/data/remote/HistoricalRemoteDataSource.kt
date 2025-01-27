package io.github.msh91.arcyto.history.data.remote

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
}
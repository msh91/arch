package io.github.msh91.arcyto.details.data.remote

import io.github.msh91.arcyto.details.data.remote.model.CoinDetailsApiModel
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinDetailsDataSource {
    /**
     * Get the details of a coin
     * param id: The id of the coin
     * param date: The date of the historical details in the format DD-MM-YYYY
     * param localization: Whether the historical details should be localized
     */
    @GET("coins/{id}/history")
    suspend fun getCoinDetails(
        @Path("id") id: String,
        @Path("date") date: String,
        @Path("localization") localization: Boolean,
    ): Result<CoinDetailsApiModel>
}
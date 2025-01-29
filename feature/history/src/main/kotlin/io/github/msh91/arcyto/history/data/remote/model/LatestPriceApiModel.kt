package io.github.msh91.arcyto.history.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A map of coin Id to price
 */
typealias CoinsLatestPricesApiModel = Map<String, LatestPriceApiModel>

@Serializable
data class LatestPriceApiModel(
    @SerialName("eur") val priceInEur: Double,
    @SerialName("eur_24h_change") val changePercentage: Double,
    @SerialName("last_updated_at") val lastUpdatedAt: Long,
)
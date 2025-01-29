package io.github.msh91.arcyto.details.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDetailsApiModel(
    @SerialName("id") val id: String,
    @SerialName("symbol") val symbol: String,
    @SerialName("name") val name: String,
    @SerialName("market_data") val marketDataApiModel: MarketDataApiModel,
)

@Serializable
data class MarketDataApiModel(
    @SerialName("current_price") val currentPrice: PriceApiModel,
    @SerialName("market_cap") val marketCap: PriceApiModel,
    @SerialName("total_volume") val totalVolume: PriceApiModel,
)
/**
 * A map of currency to price
 */
typealias PriceApiModel = Map<String, Double>
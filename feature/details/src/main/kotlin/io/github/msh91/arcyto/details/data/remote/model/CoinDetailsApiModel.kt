package io.github.msh91.arcyto.details.data.remote.model

import kotlinx.serialization.SerialName

data class CoinDetailsApiModel(
    @SerialName("id") val id: String,
    @SerialName("symbol") val symbol: String,
    @SerialName("name") val name: String,
    @SerialName("market_data") val marketDataApiModel: MarketDataApiModel,
)

data class MarketDataApiModel(
    @SerialName("current_price") val currentPrice: PriceApiModel,
    @SerialName("market_cap") val marketCap: PriceApiModel,
)
/**
 * A map of currency to price
 */
typealias PriceApiModel = Map<String, Double>
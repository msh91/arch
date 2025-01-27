package io.github.msh91.arcyto.details.domain.model

data class CoinDetails(
    val id: String,
    val name: String,
    val symbol: String,
    val marketData: List<MarketData>,
)


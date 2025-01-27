package io.github.msh91.arcyto.details.domain.model

data class MarketData(
    val currency: Currency,
    val currentPrice: Double,
    val marketCap: Double,
)
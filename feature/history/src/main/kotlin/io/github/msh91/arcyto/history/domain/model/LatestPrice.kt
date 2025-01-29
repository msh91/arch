package io.github.msh91.arcyto.history.domain.model

data class LatestPrice(
    val coinId: String,
    val price: Double,
    val lastUpdated: Long,
    val changePercentage: Double,
)

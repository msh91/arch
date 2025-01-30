package io.github.msh91.arcyto.history.domain.model

data class LatestPriceRequest(
    val coinId: String,
    val currency: String,
    val precision: Int,
    val intervalMs: Long,
)

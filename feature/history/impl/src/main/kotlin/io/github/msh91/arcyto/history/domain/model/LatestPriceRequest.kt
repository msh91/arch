package io.github.msh91.arcyto.history.domain.model

import kotlinx.coroutines.CoroutineScope

data class LatestPriceRequest(
    val coinId: String,
    val currency: String,
    val precision: Int,
    val intervalMs: Long,
    val coroutineScope: CoroutineScope,
)

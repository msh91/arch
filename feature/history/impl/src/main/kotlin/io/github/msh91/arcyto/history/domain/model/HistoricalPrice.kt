package io.github.msh91.arcyto.history.domain.model

data class HistoricalPrice(
    val date: Long,
    val value: Double,
    // difference percentage compare to the previous price
    // null if there is no previous price
    val changePercentage: Double? = null,
)

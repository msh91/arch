package io.github.msh91.arcyto.details.data.mapper

import io.github.msh91.arcyto.details.data.remote.model.CoinDetailsApiModel
import io.github.msh91.arcyto.details.data.remote.model.MarketDataApiModel
import io.github.msh91.arcyto.details.data.remote.model.PriceApiModel
import io.github.msh91.arcyto.details.domain.model.CoinDetails
import io.github.msh91.arcyto.details.domain.model.Currency
import io.github.msh91.arcyto.details.domain.model.MarketData

fun CoinDetailsApiModel.toDomainModel() = CoinDetails(
    id = id,
    name = name,
    symbol = symbol,
    marketData = marketDataApiModel.toDomainModel(),
)

fun MarketDataApiModel.toDomainModel() = currentPrice
    .filterSupportedKeys()
    .map {
        MarketData(
            currency = it.key,
            currentPrice = it.value,
            marketCap = marketCap.getValue(it.key.key)
        )
    }

fun PriceApiModel.filterSupportedKeys() = this
    .filterKeys { key -> Currency.entries.any { it.key == key } }
    .mapKeys { Currency.fromKey(it.key) }
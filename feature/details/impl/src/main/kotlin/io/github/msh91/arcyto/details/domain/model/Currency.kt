package io.github.msh91.arcyto.details.domain.model

enum class Currency(val key: String) {
    USD("usd"),
    EUR("eur"),
    GBP("gbp");

    companion object {
        fun fromKey(key: String): Currency {
            return Currency.entries.first { currency -> currency.key == key }
        }
    }
}
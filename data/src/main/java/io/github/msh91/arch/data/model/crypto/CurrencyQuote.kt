package io.github.msh91.arch.data.model.crypto

import com.google.gson.annotations.SerializedName

data class CurrencyQuote(
        @SerializedName("price") val price: Number,
        @SerializedName("percent_change_1h") val percentChange1h: Number,
        @SerializedName("percent_change_24h") val percentChange24h: Number,
        @SerializedName("percent_change_7d") val percentChange7d: Number,
        @SerializedName("last_updated") val lastUpdated: Number
)

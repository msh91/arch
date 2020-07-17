package io.github.msh91.arch.data.model.crypto

import com.google.gson.annotations.SerializedName

data class CryptoCurrency(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("quote") val quotes: Map<String, CurrencyQuote>
)

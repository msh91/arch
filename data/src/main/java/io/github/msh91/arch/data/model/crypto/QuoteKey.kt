package io.github.msh91.arch.data.model.crypto

import com.google.gson.annotations.SerializedName

enum class QuoteKey {
  @SerializedName("USD")
  USD,
  @SerializedName("BTC")
  BTC
}
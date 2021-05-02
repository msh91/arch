package io.github.msh91.arch.ui.home.list

import io.github.msh91.arch.data.model.crypto.CryptoCurrency

data class CryptoCurrencyItem(
    val cryptoCurrency: CryptoCurrency,
    val name: String,
    val price: String,
    val percent: String,
    val percentColor: Int
)

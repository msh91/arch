package io.github.msh91.arcyto.core.formatter.price

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import java.text.NumberFormat
import java.util.Currency as JavaCurrency

interface FormatPriceUseCase {
    /**
     * Format the given price to a human-readable price.
     */
    operator fun invoke(
        price: Double,
        currency: String,
    ): String
}

@Inject
@ContributesBinding(AppScope::class)
class FormatPriceUseCaseImpl : FormatPriceUseCase {
    override fun invoke(
        price: Double,
        currency: String,
    ): String {
        val formatter = NumberFormat.getCurrencyInstance()
        formatter.currency = JavaCurrency.getInstance(currency.uppercase())
        return formatter.format(price)
    }
}

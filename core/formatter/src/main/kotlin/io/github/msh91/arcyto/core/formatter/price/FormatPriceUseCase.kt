package io.github.msh91.arcyto.core.formatter.price

import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.di.scope.AppScope
import java.text.NumberFormat
import javax.inject.Inject
import java.util.Currency as JavaCurrency

interface FormatPriceUseCase {

    /**
     * Format the given price to a human-readable price.
     */
    operator fun invoke(price: Double, currency: String): String
}

@ContributesBinding(AppScope::class)
class FormatPriceUseCaseImpl @Inject constructor() : FormatPriceUseCase {

    override fun invoke(price: Double, currency: String): String {
        val formatter = NumberFormat.getCurrencyInstance()
        formatter.currency = JavaCurrency.getInstance(currency.uppercase())
        return formatter.format(price)
    }
}
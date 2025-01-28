package io.github.msh91.arcyto.details.ui.details

import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.details.domain.model.Currency
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject
import java.util.Currency as JavaCurrency

interface DetailsPriceFormatter {
    fun format(price: Double, currency: Currency? = null): String
}

@ContributesBinding(AppScope::class)
class DetailsPriceFormatterImpl @Inject constructor() : DetailsPriceFormatter {

    /**
     * Format the given price to a human-readable format. Add a currency symbol and a thousands separator.
     */
    override fun format(price: Double, currency: Currency?): String {
        val formatter = NumberFormat.getCurrencyInstance()
        formatter.currency =
            currency?.key?.let { JavaCurrency.getInstance(it) } ?: JavaCurrency.getInstance(Locale.getDefault())
        return formatter.format(price)
    }
}
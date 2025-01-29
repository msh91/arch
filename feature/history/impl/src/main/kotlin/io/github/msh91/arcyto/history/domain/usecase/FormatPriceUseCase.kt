package io.github.msh91.arcyto.history.domain.usecase

import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.di.scope.AppScope
import javax.inject.Inject

interface FormatPriceUseCase {
    operator fun invoke(price: Double, currency: String): String
}

@ContributesBinding(AppScope::class)
class FormatPriceUseCaseImpl @Inject constructor() : FormatPriceUseCase {

    override fun invoke(price: Double, currency: String): String {
        return "${getCurrencySymbol(currency)}${price.toLong()} "
    }

    private fun getCurrencySymbol(currency: String): String = when (currency) {
        "usd" -> "$"
        "eur" -> "€"
        "gbp" -> "£"
        else -> currency.uppercase()
    }
}
package io.github.msh91.arch.ui.home.list

import android.util.Log
import androidx.lifecycle.viewModelScope
import arrow.core.Either.Left
import arrow.core.Either.Right
import io.github.msh91.arch.R
import io.github.msh91.arch.data.model.movie.Movie
import io.github.msh91.arch.data.model.Error
import io.github.msh91.arch.data.model.crypto.CryptoCurrency
import io.github.msh91.arch.data.repository.crypto.CryptoRepository
import io.github.msh91.arch.ui.base.BaseViewModel
import io.github.msh91.arch.util.livedata.NonNullLiveData
import io.github.msh91.arch.util.providers.BaseResourceProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeListViewModel @Inject constructor(
        private val cryptoRepository: CryptoRepository,
        private val resourceProvider: BaseResourceProvider
) : BaseViewModel() {

    val cryptoCurrencies = NonNullLiveData<List<CryptoCurrencyItem>>(emptyList())

    init {
        getAllMovies()
    }

    private fun getAllMovies() {
        viewModelScope.launch {
            when (val either = cryptoRepository.getLatestUpdates()) {
                is Right -> cryptoCurrencies.value = mapCurrenciesToItems(either.b)
                is Left -> showError(either.a)
            }
        }
    }

    private fun mapCurrenciesToItems(currencies: List<CryptoCurrency>): List<CryptoCurrencyItem> {
        return currencies.map { currency ->
            val quote = currency.quotes["USD"] ?: error("USD quote is a must!")
            CryptoCurrencyItem(
                    currency.name,
                    resourceProvider.getString(R.string.holder_usd_price, quote.price),
                    resourceProvider.getString(R.string.holder_percent, quote.percentChange24h),
                    resourceProvider.getColor(if (quote.percentChange24h < 0) R.color.red else R.color.green)
            )
        }
    }

    private fun showError(error: Error) {
        Log.d(TAG, "showError() called  with: error = [$error]")
    }

    fun onItemClicked(cryptoCurrencyItem: CryptoCurrencyItem) {
        Log.d(TAG, "onItemClicked() called  with: cryptoCurrencyItem = [$cryptoCurrencyItem]")
    }

    companion object {
        private const val TAG = "HomeListViewModel"
    }
}
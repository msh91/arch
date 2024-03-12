package io.github.msh91.arch.ui.home.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Either.Left
import arrow.core.Either.Right
import io.github.msh91.arch.R
import io.github.msh91.arch.data.model.Error
import io.github.msh91.arch.data.model.HttpError
import io.github.msh91.arch.data.model.crypto.CryptoCurrency
import io.github.msh91.arch.data.model.crypto.QuoteKey
import io.github.msh91.arch.data.repository.crypto.CryptoRepository
import io.github.msh91.arch.ui.base.BaseViewModel
import io.github.msh91.arch.ui.home.HomeNavigator
import io.github.msh91.arch.util.livedata.NonNullLiveData
import io.github.msh91.arch.util.livedata.SingleEventLiveData
import io.github.msh91.arch.util.provider.BaseResourceProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class LatestUpdatesViewModel @Inject constructor(
    private val cryptoRepository: CryptoRepository,
    private val resourceProvider: BaseResourceProvider,
    private val homeNavigator: HomeNavigator
) : BaseViewModel() {

    val cryptoCurrencyItemsLiveData = NonNullLiveData<List<CryptoCurrencyItem>>(emptyList())
    val errorLiveData = SingleEventLiveData<String>()
    val loadingLiveData = MutableLiveData<Boolean>()
    private var callInitialized = false

    override fun onStart() {
        super.onStart()
        if (!callInitialized) {
            getLatestUpdates()
        }
    }

    private fun getLatestUpdates() {
        viewModelScope.launch {
            callInitialized = true
            loadingLiveData.value = true
            when (val either = cryptoRepository.getLatestUpdates()) {
                is Right -> showCryptoCurrencies(either.value)
                is Left -> showError(either.value)
            }
            loadingLiveData.value = false
        }
    }

    private fun showCryptoCurrencies(currencies: List<CryptoCurrency>) {
        if (!areCurrenciesValid(currencies)) {
            showError(HttpError.InvalidResponse(100, resourceProvider.getString(R.string.error_invalid_quote)))
            return
        }
        this.cryptoCurrencyItemsLiveData.value = mapCurrenciesToItems(currencies)
    }

    private fun areCurrenciesValid(currencies: List<CryptoCurrency>): Boolean {
        return currencies.all { it.quotes.containsKey(QuoteKey.USD) }
    }

    private fun mapCurrenciesToItems(currencies: List<CryptoCurrency>): List<CryptoCurrencyItem> {
        return currencies.map { currency ->
            val quote = currency.quotes[QuoteKey.USD]!!
            CryptoCurrencyItem(
                currency,
                currency.name,
                resourceProvider.getString(R.string.holder_usd_price, quote.price),
                resourceProvider.getString(R.string.holder_percent, quote.percentChange24h),
                resourceProvider.getColor(if (quote.percentChange24h < 0) R.color.red else R.color.green)
            )
        }
    }

    private fun showError(error: Error) {
        errorLiveData.value = resourceProvider.getErrorMessage(error)
    }

    fun onItemClicked(item: CryptoCurrencyItem) {
        if (item.cryptoCurrency.id != BITCOIN_ID) {
            errorLiveData.value = resourceProvider.getString(R.string.error_chart_not_provided)
            return
        }
        fragmentAction { homeNavigator.navigateToChartFragment(it) }
    }

    companion object {
        private const val TAG = "LatestUpdatesViewModel"
        private const val BITCOIN_ID = 1
    }
}

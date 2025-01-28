package io.github.msh91.arcyto.details.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.anvil.annotations.ContributesMultibinding
import io.github.msh91.arcyto.core.di.scope.MainScreenScope
import io.github.msh91.arcyto.core.di.viewmodel.ViewModelKey
import io.github.msh91.arcyto.details.data.repository.CoinDetailsRepository
import io.github.msh91.arcyto.details.domain.model.CoinDetails
import io.github.msh91.arcyto.details.domain.model.CoinDetailsRequest
import io.github.msh91.arcyto.details.domain.model.Currency
import io.github.msh91.arcyto.details.domain.model.MarketData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ContributesMultibinding(
    scope = MainScreenScope::class,
    boundType = ViewModel::class
)
@ViewModelKey(DetailsViewModel::class)
class DetailsViewModel @Inject constructor(
    private val detailsRepository: CoinDetailsRepository,
    private val priceFormatter: DetailsPriceFormatter,
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var coinDetails: CoinDetails? = null

    init {
        fetchCoinDetails()
    }

    private fun fetchCoinDetails() {
        viewModelScope.launch {
            detailsRepository
                .getCoinDetails(CoinDetailsRequest("bitcoin", "20-01-2025", false))
                .fold(onSuccess = ::onCoinDetailsReceived, onFailure = ::onErrorReceived)
        }
    }

    private fun onCoinDetailsReceived(coinDetails: CoinDetails) {
        this.coinDetails = coinDetails
        _uiState.value = coinDetails.toUiState()
    }

    private fun onErrorReceived(throwable: Throwable) {
        Log.e("sdsdsd", "Error fetching coin details", throwable)
    }

    fun onCurrencySelected(currency: Currency) {
        Log.d("sdsdsd", "onCurrencySelected() called with: currency = $currency")
        val coinDetails = this.coinDetails ?: return
        val currentState = _uiState.value
        if (currentState is DetailsUiState.Success) {
            val selectedMarketData = coinDetails.marketDataList.first { it.currency == currency }
            Log.d("Sdsdsd", "onCurrencySelected() returned: $selectedMarketData")
            _uiState.value = currentState.copy(
                detailsUiModel = currentState.detailsUiModel.copy(
                    selectedMarketData = selectedMarketData.toUiModel()
                )
            )
        }
    }

    private fun CoinDetails.toUiState(): DetailsUiState {
        val defaultData = marketDataList.first().toUiModel()
        return DetailsUiState.Success(
            CoinDetailsUiModel(
                name = name,
                symbol = symbol,
                currentPriceDefault = defaultData.currentPrice,
                marketDataList = marketDataList.map { it.toUiModel() },
                selectedMarketData = defaultData,
            )
        )
    }

    private fun MarketData.toUiModel(): MarketDataUiModel {
        return MarketDataUiModel(
            currency = currency,
            currencyTitle = currency.key.uppercase(),
            currentPrice = priceFormatter.format(currentPrice, currency),
            marketCap = priceFormatter.format(marketCap, currency),
            totalVolume = priceFormatter.format(totalVolume, currency),
        )
    }
}
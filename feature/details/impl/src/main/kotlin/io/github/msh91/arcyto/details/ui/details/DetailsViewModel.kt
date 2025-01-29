package io.github.msh91.arcyto.details.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.anvil.annotations.ContributesMultibinding
import io.github.msh91.arcyto.core.data.remote.RemoteErrorMapper
import io.github.msh91.arcyto.core.di.scope.MainScreenScope
import io.github.msh91.arcyto.core.di.viewmodel.ViewModelKey
import io.github.msh91.arcyto.details.data.repository.CoinDetailsRepository
import io.github.msh91.arcyto.details.domain.model.CoinDetails
import io.github.msh91.arcyto.details.domain.model.CoinDetailsRequest
import io.github.msh91.arcyto.details.domain.model.Currency
import io.github.msh91.arcyto.details.domain.model.MarketData
import io.github.msh91.arcyto.details.api.navigation.DetailsRouteRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@ContributesMultibinding(
    scope = MainScreenScope::class,
    boundType = ViewModel::class
)
@ViewModelKey(DetailsViewModel::class)
class DetailsViewModel @Inject constructor(
    private val detailsRepository: CoinDetailsRepository,
    private val priceFormatter: DetailsPriceFormatter,
    private val errorMapper: RemoteErrorMapper,
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var coinDetails: CoinDetails? = null
    private lateinit var request: DetailsRouteRequest

    fun fetchCoinDetails(request: DetailsRouteRequest) {
        this.request = request
        viewModelScope.launch {
            val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(request.date)
            detailsRepository
                .getCoinDetails(CoinDetailsRequest(request.coinId, date, false))
                .fold(onSuccess = ::onCoinDetailsReceived, onFailure = ::onErrorReceived)
        }
    }

    private fun onCoinDetailsReceived(coinDetails: CoinDetails) {
        this.coinDetails = coinDetails
        _uiState.value = coinDetails.toUiState()
    }

    private fun onErrorReceived(throwable: Throwable) {
        _uiState.value = DetailsUiState.Error(
            message = errorMapper.getErrorMessage(throwable)
        )
    }

    fun onCurrencySelected(currency: Currency) {
        val coinDetails = this.coinDetails ?: return
        val currentState = _uiState.value
        if (currentState is DetailsUiState.Success) {
            val selectedMarketData = coinDetails.marketDataList.first { it.currency == currency }
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
                date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(request.date),
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
package io.github.msh91.arcyto.details.ui.details

import io.github.msh91.arcyto.details.domain.model.Currency

sealed interface DetailsUiState {
    data object Loading : DetailsUiState

    data class Success(val detailsUiModel: CoinDetailsUiModel) : DetailsUiState

    data class Error(val message: String) : DetailsUiState
}

data class CoinDetailsUiModel(
    val name: String,
    val symbol: String,
    val date: String,
    val currentPriceDefault: String,
    val marketDataList: List<MarketDataUiModel>,
    val selectedMarketData: MarketDataUiModel,
)

data class MarketDataUiModel(
    val currency: Currency,
    val currencyTitle: String,
    val currentPrice: String,
    val marketCap: String,
    val totalVolume: String,
)
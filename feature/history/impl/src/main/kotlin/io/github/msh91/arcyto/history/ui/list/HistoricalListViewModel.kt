package io.github.msh91.arcyto.history.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.anvil.annotations.ContributesMultibinding
import io.github.msh91.arcyto.core.design.component.PerformanceValue
import io.github.msh91.arcyto.core.di.common.CompositeErrorMapper
import io.github.msh91.arcyto.core.di.scope.MainScreenScope
import io.github.msh91.arcyto.core.di.viewmodel.ViewModelKey
import io.github.msh91.arcyto.core.formatter.date.DateFormat
import io.github.msh91.arcyto.core.formatter.date.DateProvider
import io.github.msh91.arcyto.core.formatter.date.FormatDateUseCase
import io.github.msh91.arcyto.core.formatter.price.FormatPriceUseCase
import io.github.msh91.arcyto.core.tooling.extension.coroutines.eventsFlow
import io.github.msh91.arcyto.core.tooling.extension.isToday
import io.github.msh91.arcyto.details.api.navigation.DetailsRouteRequest
import io.github.msh91.arcyto.history.domain.model.HistoricalChartRequest
import io.github.msh91.arcyto.history.domain.model.HistoricalPrice
import io.github.msh91.arcyto.history.domain.model.LatestPrice
import io.github.msh91.arcyto.history.domain.model.LatestPriceRequest
import io.github.msh91.arcyto.history.domain.usecase.GetHistoricalChartUseCase
import io.github.msh91.arcyto.history.domain.usecase.GetLatestPriceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

/**
 * Configuration for the historical data list
 *
 * @property coinId The ID of the cryptocurrency to fetch
 * @property currency The currency to get prices in
 * @property pricePrecision The decimal precision of price values
 * @property priceUpdateIntervalMs Interval for price updates in milliseconds
 * @property historicalDays Number of historical days to fetch
 * @property historicalInterval Interval between historical data points
 */
data class HistoricalListConfig(
    val coinId: String = "bitcoin",
    val currency: String = "eur",
    val pricePrecision: Int = 2,
    val priceUpdateIntervalMs: Long = 60_000L,
    val historicalDays: Int = 15,
    val historicalInterval: String = "daily"
)

/**
 * ViewModel responsible for managing historical price data and latest price updates
 */
@ContributesMultibinding(
    scope = MainScreenScope::class,
    boundType = ViewModel::class
)
@ViewModelKey(HistoricalListViewModel::class)
class HistoricalListViewModel @Inject constructor(
    private val getHistoricalChartUseCase: GetHistoricalChartUseCase,
    private val getLatestPriceUseCase: GetLatestPriceUseCase,
    private val errorMapper: CompositeErrorMapper,
    private val formatDateUseCase: FormatDateUseCase,
    private val formatPriceUseCase: FormatPriceUseCase,
    private val dateProvider: DateProvider,
) : ViewModel() {

    private val config = HistoricalListConfig()
    
    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState = _uiState.asStateFlow()
    
    private val _events = viewModelScope.eventsFlow<HistoricalListUiEvent>()
    val events: Flow<HistoricalListUiEvent> = _events

    init {
        fetchHistoricalList()
        observeLatestPrice()
    }

    private fun observeLatestPrice() {
        viewModelScope.launch {
            val request = LatestPriceRequest(
                coinId = config.coinId,
                currency = config.currency,
                precision = config.pricePrecision,
                intervalMs = config.priceUpdateIntervalMs,
            )
            getLatestPriceUseCase(request)
                .collectLatest { result ->
                    result.fold(
                        onSuccess = ::updateLatestPrice,
                        onFailure = ::handleError
                    )
                }
        }
    }

    private fun updateLatestPrice(latestPrice: LatestPrice) {
        _uiState.update { currentState ->
            when (currentState) {
                is HistoryUiState.Success -> currentState.copy(
                    currentPriceUiModel = latestPrice.toUiModel()
                )
                HistoryUiState.Loading -> HistoryUiState.Success(
                    currentPriceUiModel = latestPrice.toUiModel(),
                    historicalValueUiModels = emptyList()
                )
            }
        }
    }

    private fun fetchHistoricalList() {
        viewModelScope.launch {
            getHistoricalChartUseCase(
                HistoricalChartRequest(
                    id = config.coinId,
                    currency = config.currency,
                    days = config.historicalDays,
                    interval = config.historicalInterval,
                    precision = config.pricePrecision
                )
            ).fold(
                onSuccess = ::updateHistoricalList,
                onFailure = ::handleError
            )
        }
    }

    private fun updateHistoricalList(historicalPrices: List<HistoricalPrice>) {
        _uiState.update { currentState ->
            when (currentState) {
                is HistoryUiState.Success -> currentState.copy(
                    historicalValueUiModels = historicalPrices.toUiModel()
                )
                HistoryUiState.Loading -> HistoryUiState.Success(
                    currentPriceUiModel = null,
                    historicalValueUiModels = historicalPrices.toUiModel()
                )
            }
        }
    }

    private fun LatestPrice.toUiModel(): PriceValueUiModel = 
        createPriceValueUiModel(dateProvider.getCurrentDate(), price, changePercentage)

    private fun List<HistoricalPrice>.toUiModel(): List<PriceValueUiModel> = this
        // remove today price from the list as it will be displayed separately via latest price component
        .filterNot { it.date.isToday() }
        .map { createPriceValueUiModel(it.date, it.value, it.changePercentage) }

    private fun createPriceValueUiModel(
        date: Long, 
        value: Double, 
        changePercentage: Double?
    ): PriceValueUiModel = PriceValueUiModel(
        date = date,
        formattedDate = formatDateUseCase(date, DateFormat.MONTH_DAY, true),
        value = formatPriceUseCase(value, config.currency),
        performanceValue = changePercentage?.let { percentage ->
            PerformanceValue(
                text = "%.2f%%".format(abs(percentage)),
                isPositive = percentage > 0
            )
        }
    )

    private fun handleError(throwable: Throwable) {
        viewModelScope.launch {
            _events.emit(HistoricalListUiEvent.ShowSnackbar(errorMapper.getErrorMessage(throwable)))
            
            if (_uiState.value is HistoryUiState.Loading) {
                _uiState.value = HistoryUiState.Success(
                    currentPriceUiModel = null,
                    historicalValueUiModels = emptyList()
                )
            }
        }
    }

    fun onItemClick(item: PriceValueUiModel) {
        viewModelScope.launch {
            _events.emit(HistoricalListUiEvent.NavigateToDetails(DetailsRouteRequest(config.coinId, item.date)))
        }
    }
}
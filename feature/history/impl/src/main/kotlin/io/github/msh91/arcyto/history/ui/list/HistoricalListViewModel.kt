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
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

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
                coinId = COIN_ID,
                currency = CURRENCY,
                precision = 2,
                intervalMs = 60_000,
            )
            getLatestPriceUseCase.invoke(request)
                .collectLatest { it.fold(onSuccess = ::onLatestPriceReceived, onFailure = ::onErrorReceived) }
        }
    }

    private fun onLatestPriceReceived(latestPrice: LatestPrice) {
        val currentState = _uiState.value as? HistoryUiState.Success
        _uiState.value = HistoryUiState.Success(
            currentPriceUiModel = latestPrice.toUiModel(),
            historicalValueUiModels = currentState?.historicalValueUiModels ?: emptyList(),
        )
    }

    private fun fetchHistoricalList() {
        viewModelScope.launch {
            getHistoricalChartUseCase
                .invoke(
                    request = HistoricalChartRequest(
                        id = COIN_ID,
                        currency = CURRENCY,
                        days = 15,
                        interval = "daily",
                        precision = 2
                    )
                )
                .fold(::onHistoricalListReceived, ::onErrorReceived)
        }
    }

    private fun onHistoricalListReceived(historicalPrices: List<HistoricalPrice>) {
        val currentState = _uiState.value as? HistoryUiState.Success
        _uiState.value = HistoryUiState.Success(
            currentPriceUiModel = currentState?.currentPriceUiModel,
            historicalValueUiModels = historicalPrices.toUiModel(),
        )
    }

    private fun LatestPrice.toUiModel() = createValueItem(dateProvider.getCurrentDate(), price, changePercentage)

    private fun List<HistoricalPrice>.toUiModel(): List<PriceValueUiModel> = this
        // remote today price from the list as it will be displayed separately via latest price component
        .filterNot { it.date.isToday() }
        .map { createValueItem(it.date, it.value, it.changePercentage) }

    private fun createValueItem(date: Long, value: Double, changePercentage: Double?): PriceValueUiModel {
        return PriceValueUiModel(
            date = date,
            formattedDate = formatDateUseCase.invoke(date, DateFormat.MONTH_DAY, true),
            value = formatPriceUseCase.invoke(value, CURRENCY),
            performanceValue = changePercentage?.let { diff ->
                PerformanceValue(
                    text = "%.2f".format(abs(diff)).plus("%"),
                    isPositive = diff > 0
                )
            },
        )
    }

    private fun onErrorReceived(throwable: Throwable) {
        viewModelScope.launch {
            _events.emit(HistoricalListUiEvent.ShowSnackbar(errorMapper.getErrorMessage(throwable)))
            if (_uiState.value is HistoryUiState.Loading) {
                _uiState.value = HistoryUiState.Success(null, emptyList())
            }
        }
    }

    fun onItemClick(item: PriceValueUiModel) {
        viewModelScope.launch {
            _events.emit(HistoricalListUiEvent.NavigateToDetails(DetailsRouteRequest(COIN_ID, item.date)))
        }
    }

    companion object {
        private const val COIN_ID = "bitcoin"
        private const val CURRENCY = "eur"
    }
}
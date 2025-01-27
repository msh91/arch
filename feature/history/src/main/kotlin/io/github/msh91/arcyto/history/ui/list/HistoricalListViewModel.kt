package io.github.msh91.arcyto.history.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.anvil.annotations.ContributesMultibinding
import io.github.msh91.arcyto.core.data.remote.RemoteErrorMapper
import io.github.msh91.arcyto.core.design.component.PerformanceValue
import io.github.msh91.arcyto.core.di.scope.MainScreenScope
import io.github.msh91.arcyto.core.di.viewmodel.ViewModelKey
import io.github.msh91.arcyto.core.tooling.extension.coroutines.eventsFlow
import io.github.msh91.arcyto.history.domain.model.HistoricalChartRequest
import io.github.msh91.arcyto.history.domain.model.HistoricalPrice
import io.github.msh91.arcyto.history.domain.usecase.FormatDateUseCase
import io.github.msh91.arcyto.history.domain.usecase.FormatPriceUseCase
import io.github.msh91.arcyto.history.domain.usecase.GetHistoricalChartUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val errorMapper: RemoteErrorMapper,
    private val formatDateUseCase: FormatDateUseCase,
    private val formatPriceUseCase: FormatPriceUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _events = viewModelScope.eventsFlow<HistoricalListUiEvent>()
    val events: Flow<HistoricalListUiEvent> = _events

    init {
        fetchHistoricalList()
    }

    private fun fetchHistoricalList() {
        viewModelScope.launch {
            getHistoricalChartUseCase
                .invoke(request = HistoricalChartRequest("bitcoin", "eur", 14, "daily", 0))
                .fold(::onHistoricalListReceived, ::onErrorReceived)
        }
    }

    private fun onHistoricalListReceived(historicalPrices: List<HistoricalPrice>) {
        _uiState.value = HistoryUiState.Success(
            historicalPrices.map {
                HistoricalValueItem(
                    date = it.date,
                    formattedDate = formatDateUseCase.invoke(it.date),
                    value = formatPriceUseCase.invoke(it.value, "eur"),
                    performanceValue = it.changePercentage?.let { diff ->
                        PerformanceValue(
                            text = "%.2f".format(abs(diff)).plus("%"),
                            isPositive = diff > 0
                        )
                    },
                )
            }
        )
    }

    private fun onErrorReceived(throwable: Throwable) {
        viewModelScope.launch {
            _events.emit(HistoricalListUiEvent.ShowSnackbar(errorMapper.getErrorMessage(throwable)))
            if (_uiState.value is HistoryUiState.Loading) {
                _uiState.value = HistoryUiState.Success(emptyList())
            }
        }
    }

    fun onItemClick(historicalValueItem: HistoricalValueItem) {
        // todo: Navigate to details screen
    }
}
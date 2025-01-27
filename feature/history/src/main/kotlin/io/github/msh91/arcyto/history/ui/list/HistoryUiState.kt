package io.github.msh91.arcyto.history.ui.list

import androidx.compose.runtime.Immutable
import io.github.msh91.arcyto.core.design.component.PerformanceValue

@Immutable
sealed interface HistoryUiState {
    object Loading : HistoryUiState

    @Immutable
    data class Success(val valueItems: List<HistoricalValueItem>) : HistoryUiState
}

data class HistoricalValueItem(
    val date: Long,
    val formattedDate: String,
    val value: String,
    val performanceValue: PerformanceValue? = null,
)
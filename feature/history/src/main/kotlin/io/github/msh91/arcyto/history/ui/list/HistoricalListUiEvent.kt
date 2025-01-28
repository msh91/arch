package io.github.msh91.arcyto.history.ui.list

sealed interface HistoricalListUiEvent {
    data class ShowSnackbar(val message: String) : HistoricalListUiEvent

    data class NavigateToDetails(val id: String) : HistoricalListUiEvent
}
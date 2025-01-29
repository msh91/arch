package io.github.msh91.arcyto.history.ui.list

import io.github.msh91.arcyto.details.api.navigation.DetailsRouteRequest

sealed interface HistoricalListUiEvent {
    data class ShowSnackbar(val message: String) : HistoricalListUiEvent

    data class NavigateToDetails(val detailsRouteRequest: DetailsRouteRequest) : HistoricalListUiEvent
}
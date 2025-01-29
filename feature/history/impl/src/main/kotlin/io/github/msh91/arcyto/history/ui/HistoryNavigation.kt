package io.github.msh91.arcyto.history.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.msh91.arcyto.details.api.navigation.DetailsRouteRequest
import io.github.msh91.arcyto.history.api.navigation.HistoricalRouteRequest
import io.github.msh91.arcyto.history.ui.list.HistoricalListRoute

fun NavGraphBuilder.historicalListScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    navigateToDetails: (DetailsRouteRequest) -> Unit,
) {
    composable<HistoricalRouteRequest> {
        HistoricalListRoute(onShowSnackbar, navigateToDetails)
    }
}
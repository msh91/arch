package io.github.msh91.arcyto.history.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.msh91.arcyto.details.ui.DetailsRouteRequest
import io.github.msh91.arcyto.history.ui.list.HistoricalListRoute
import kotlinx.serialization.Serializable

@Serializable
object HistoricalListRoute

fun NavGraphBuilder.historicalListScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    navigateToDetails: (DetailsRouteRequest) -> Unit,
) {
    composable<HistoricalListRoute> {
        HistoricalListRoute(onShowSnackbar, navigateToDetails)
    }
}
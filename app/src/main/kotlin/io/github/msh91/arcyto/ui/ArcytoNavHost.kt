package io.github.msh91.arcyto.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.github.msh91.arcyto.details.ui.detailsScreen
import io.github.msh91.arcyto.history.api.navigation.HistoricalRouteRequest
import io.github.msh91.arcyto.history.ui.historicalListScreen

@Composable
fun ArcytoNavHost(
    appState: AppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = HistoricalRouteRequest,
        modifier = modifier,
    ) {
        historicalListScreen(
            onShowSnackbar = onShowSnackbar,
            navigateToDetails = { navController.navigate(it) }
        )

        detailsScreen()
    }
}
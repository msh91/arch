package io.github.msh91.arcyto.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.github.msh91.arcyto.details.ui.DetailsRoute
import io.github.msh91.arcyto.details.ui.detailsScreen
import io.github.msh91.arcyto.history.ui.HistoricalListRoute
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
        startDestination = HistoricalListRoute,
        modifier = modifier,
    ) {
        historicalListScreen(
            onShowSnackbar = onShowSnackbar,
            navigateToDetails = { navController.navigate(DetailsRoute) }
        )

        detailsScreen(onShowSnackbar)
    }
}
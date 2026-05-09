package io.github.msh91.arcyto.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import io.github.msh91.arcyto.details.ui.detailsScreen
import io.github.msh91.arcyto.history.ui.historicalListScreen

@Composable
fun ArcytoNavDisplay(
    appState: AppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val backStack = appState.backStack
    NavDisplay(
        backStack = backStack,
        entryProvider =
            entryProvider {
                historicalListScreen(
                    onShowSnackbar = onShowSnackbar,
                    navigateToDetails = { backStack.add(it) },
                )

                detailsScreen(onNavigateBack = { backStack.removeLastOrNull() })
            },
        onBack = { backStack.removeLastOrNull() },
        modifier = modifier,
    )
}

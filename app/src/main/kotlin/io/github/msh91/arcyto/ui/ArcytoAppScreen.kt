package io.github.msh91.arcyto.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import io.github.msh91.arcyto.history.api.navigation.HistoricalRouteRequest

@Stable
data class AppState(
    val backStack: SnapshotStateList<NavKey>,
)

@Composable
fun rememberAppState(): AppState {
    val backStack = remember { mutableStateListOf<NavKey>(HistoricalRouteRequest) }
    return remember(backStack) { AppState(backStack) }
}

@Composable
fun ArcytoAppScreen(
    factory: MetroViewModelFactory,
    modifier: Modifier = Modifier,
) {
    val appState = rememberAppState()
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(LocalMetroViewModelFactory provides factory) {
        Scaffold(
            modifier = modifier,
            containerColor = colorScheme.background,
            contentColor = colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            Box(
                modifier =
                    Modifier
                        .padding(padding)
                        .statusBarsPadding(),
            ) {
                ArcytoNavDisplay(
                    appState = appState,
                    modifier = modifier.padding(padding),
                    onShowSnackbar = { message, action ->
                        snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = action,
                            duration = SnackbarDuration.Short,
                        ) == ActionPerformed
                    },
                )
            }
        }
    }
}

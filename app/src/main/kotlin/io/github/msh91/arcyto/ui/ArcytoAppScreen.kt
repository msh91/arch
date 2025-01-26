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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.github.msh91.arcyto.core.di.viewmodel.LocalViewModelProviderFactory

@Stable
data class AppState(
    val navController: NavHostController,
)

@Composable
fun rememberAppState(navController: NavHostController = rememberNavController()): AppState {
    return remember(navController) {
        AppState(navController)
    }
}

@Composable
fun ArcytoAppScreen(factory: ViewModelProvider.Factory, modifier: Modifier = Modifier) {
    val appState = rememberAppState()
    val snackbarHostState = remember { SnackbarHostState() }

    WithDependencies(factory) {
        Scaffold(
            modifier = modifier,
            containerColor = colorScheme.background,
            contentColor = colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .statusBarsPadding(),
            ) {
                ArcytoNavHost(
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

@Composable
internal fun WithDependencies(
    viewModelProviderFactory: ViewModelProvider.Factory,
    content: @Composable () -> Unit,
) {
    val factory = remember { viewModelProviderFactory }
    CompositionLocalProvider(LocalViewModelProviderFactory provides factory) {
        content()
    }
}
package io.github.msh91.arcyto.details.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.msh91.arcyto.details.ui.details.DetailsRoute
import kotlinx.serialization.Serializable

@Serializable
object DetailsRoute

fun NavGraphBuilder.detailsScreen(onShowSnackbar: suspend (String, String?) -> Boolean) {
    composable<DetailsRoute> {
        DetailsRoute(onShowSnackbar)
    }
}
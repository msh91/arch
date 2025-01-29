package io.github.msh91.arcyto.details.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.github.msh91.arcyto.details.ui.details.DetailsRoute
import kotlinx.serialization.Serializable

@Serializable
data class DetailsRouteRequest(val coinId: String, val date: Long)

fun NavGraphBuilder.detailsScreen(onShowSnackbar: suspend (String, String?) -> Boolean) {
    composable<DetailsRouteRequest> {
        DetailsRoute(it.toRoute(), onShowSnackbar)
    }
}
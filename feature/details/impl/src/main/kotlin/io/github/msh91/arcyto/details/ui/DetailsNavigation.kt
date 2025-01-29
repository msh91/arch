package io.github.msh91.arcyto.details.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.github.msh91.arcyto.details.api.navigation.DetailsRouteRequest
import io.github.msh91.arcyto.details.ui.details.DetailsRoute

fun NavGraphBuilder.detailsScreen() {
    composable<DetailsRouteRequest> {
        DetailsRoute(it.toRoute())
    }
}
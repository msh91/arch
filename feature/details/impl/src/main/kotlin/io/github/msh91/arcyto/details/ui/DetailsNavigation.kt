package io.github.msh91.arcyto.details.ui

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.msh91.arcyto.details.api.navigation.DetailsRouteRequest
import io.github.msh91.arcyto.details.ui.details.DetailsRoute

fun EntryProviderScope<NavKey>.detailsScreen(onNavigateBack: () -> Unit) {
    entry<DetailsRouteRequest> { key ->
        DetailsRoute(key, onNavigateBack)
    }
}

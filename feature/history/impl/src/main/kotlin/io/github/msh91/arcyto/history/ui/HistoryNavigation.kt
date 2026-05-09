package io.github.msh91.arcyto.history.ui

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.msh91.arcyto.details.api.navigation.DetailsRouteRequest
import io.github.msh91.arcyto.history.api.navigation.HistoricalRouteRequest
import io.github.msh91.arcyto.history.ui.list.HistoricalListRoute

fun EntryProviderScope<NavKey>.historicalListScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    navigateToDetails: (DetailsRouteRequest) -> Unit,
) {
    entry(HistoricalRouteRequest) {
        HistoricalListRoute(onShowSnackbar, navigateToDetails)
    }
}

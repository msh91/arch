package io.github.msh91.arcyto.history.ui.list

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import io.github.msh91.arcyto.core.design.component.ArcBitcoinIcon
import io.github.msh91.arcyto.core.design.component.ArcHistoricalListLoading
import io.github.msh91.arcyto.core.design.component.ArcPerformance
import io.github.msh91.arcyto.core.design.component.LiveAnimationCircle
import io.github.msh91.arcyto.core.design.component.PerformanceValue
import io.github.msh91.arcyto.core.design.theme.ArcytoTheme
import io.github.msh91.arcyto.core.di.viewmodel.arcytoViewModel
import io.github.msh91.arcyto.details.api.navigation.DetailsRouteRequest
import io.github.msh91.arcyto.history.ui.list.HistoricalListUiEvent.ShowSnackbar

@Composable
fun HistoricalListRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    navigateToDetails: (DetailsRouteRequest) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoricalListViewModel = arcytoViewModel(),
) {
    val event by viewModel.events.collectAsStateWithLifecycle(initialValue = null)
    LaunchedEffect(event) {
        when (event) {
            is HistoricalListUiEvent.NavigateToDetails -> {
                navigateToDetails((event as HistoricalListUiEvent.NavigateToDetails).detailsRouteRequest)
            }

            is ShowSnackbar -> onShowSnackbar((event as ShowSnackbar).message, null)
            null -> {}
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HistoricalListScreen(uiState, viewModel::onItemClick, modifier)
}

@Composable
internal fun HistoricalListScreen(
    uiState: HistoryUiState,
    onItemClick: (PriceValueUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        when (uiState) {
            HistoryUiState.Loading -> LoadingState(modifier)
            is HistoryUiState.Success -> PriceList(uiState, onItemClick, modifier)
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        ArcHistoricalListLoading()
    }
}

@Composable
private fun PriceList(
    successUiState: HistoryUiState.Success,
    onItemClick: (PriceValueUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier,
            contentPadding = PaddingValues(all = 8.dp),
        ) {
            successUiState.currentPriceUiModel?.let {
                item {
                    Price(it, onItemClick, true)
                }
            }
            items(items = successUiState.historicalValueUiModels, key = { it.date }) {
                Price(it, onItemClick, false)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Price(
    priceValueUiModel: PriceValueUiModel,
    onItemClick: (PriceValueUiModel) -> Unit,
    isCurrentPrice: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = cardElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
        colors = cardColors(containerColor = colorScheme.surface),
        onClick = { onItemClick(priceValueUiModel) },
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            ArcBitcoinIcon(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            )
            Text(
                text = priceValueUiModel.formattedDate,
                style = typography.labelSmall,
                color = colorScheme.onSurface,
                maxLines = 1,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(all = 12.dp),
            )
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                priceValueUiModel.performanceValue?.let {
                    ArcPerformance(
                        value = it,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = priceValueUiModel.value,
                        style = typography.titleLarge,
                        maxLines = 1,
                        fontWeight = Bold,
                        color = colorScheme.onSurface,
                    )
                    if (isCurrentPrice) {
                        LiveAnimationCircle()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HistoricalListScreenPreview() {
    ArcytoTheme {
        HistoricalListScreen(
            uiState = HistoryUiState.Success(
                currentPriceUiModel = PriceValueUiModel(
                    date = 1,
                    formattedDate = "Today",
                    value = "99,000 $",
                    performanceValue = PerformanceValue("4000", true),
                ),
                historicalValueUiModels = listOf(
                    PriceValueUiModel(
                        date = 2,
                        formattedDate = "Yesterday",
                        value = "95000 $",
                        performanceValue = PerformanceValue("5000", false),
                    ),
                    PriceValueUiModel(
                        date = 3,
                        formattedDate = "Friday",
                        value = "100000 $",
                        performanceValue = PerformanceValue("1000", true),
                    ),
                    PriceValueUiModel(
                        date = 4,
                        formattedDate = "Friday",
                        value = "100000 $",
                        performanceValue = null,
                    ),
                )
            ),
            onItemClick = { },
            modifier = Modifier,
        )
    }
}
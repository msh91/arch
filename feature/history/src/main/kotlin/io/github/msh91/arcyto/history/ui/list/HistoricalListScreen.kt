package io.github.msh91.arcyto.history.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import io.github.msh91.arcyto.core.design.component.PerformanceValue
import io.github.msh91.arcyto.core.design.theme.ArcytoTheme
import io.github.msh91.arcyto.core.di.viewmodel.arcytoViewModel
import io.github.msh91.arcyto.history.ui.list.HistoricalListUiEvent.ShowSnackbar

@Composable
fun HistoricalListRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: HistoricalListViewModel = arcytoViewModel(),
) {
    val event by viewModel.events.collectAsStateWithLifecycle(null)
    LaunchedEffect(event) {
        if (event is ShowSnackbar) {
            onShowSnackbar((event as ShowSnackbar).message, null)
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HistoricalListScreen(uiState, viewModel::onItemClick, modifier)
}

@Composable
internal fun HistoricalListScreen(
    uiState: HistoryUiState,
    onItemClick: (HistoricalValueItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        HistoryUiState.Loading -> LoadingState(modifier)
        is HistoryUiState.Success -> HistoricalList(uiState.valueItems, onItemClick, modifier)
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        ArcHistoricalListLoading()
    }
}

@Composable
private fun HistoricalList(
    valueItems: List<HistoricalValueItem>,
    onItemClick: (HistoricalValueItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier,
            contentPadding = PaddingValues(all = 8.dp),
        ) {
            items(items = valueItems, key = { it.date }) {
                HistoricalValue(it, onItemClick)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun HistoricalValue(
    valueItem: HistoricalValueItem,
    onItemClick: (HistoricalValueItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = cardElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
        colors = cardColors(containerColor = colorScheme.background),
        onClick = { onItemClick(valueItem) },
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
                text = valueItem.formattedDate,
                style = typography.labelSmall,
                maxLines = 1,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(all = 12.dp),
            )
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                valueItem.performanceValue?.let {
                    ArcPerformance(
                        value = it,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Text(
                    text = valueItem.value,
                    style = typography.titleLarge,
                    maxLines = 1,
                    fontWeight = Bold,
                    modifier = Modifier
                        .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoricalListScreenPreview() {
    ArcytoTheme {
        HistoricalListScreen(
            uiState = HistoryUiState.Success(
                listOf(
                    HistoricalValueItem(
                        date = 1,
                        formattedDate = "Today",
                        value = "99,000 $",
                        performanceValue = PerformanceValue("4000", true),
                    ),
                    HistoricalValueItem(
                        date = 2,
                        formattedDate = "Yesterday",
                        value = "95000 $",
                        performanceValue = PerformanceValue("5000", false),
                    ),
                    HistoricalValueItem(
                        date = 3,
                        formattedDate = "Friday",
                        value = "100000 $",
                        performanceValue = PerformanceValue("1000", true),
                    ),
                    HistoricalValueItem(
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
package io.github.msh91.arcyto.details.ui.details

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.msh91.arcyto.core.design.component.ArcBitcoinIcon
import io.github.msh91.arcyto.core.design.component.ArcDetailsListLoading
import io.github.msh91.arcyto.core.design.component.ArcSwitcher
import io.github.msh91.arcyto.core.design.theme.ArcytoTheme
import io.github.msh91.arcyto.core.di.viewmodel.arcytoViewModel
import io.github.msh91.arcyto.details.R
import io.github.msh91.arcyto.details.domain.model.Currency

@Composable
fun DetailsRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = arcytoViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetailsScreen(uiState, viewModel::onCurrencySelected, modifier)
}

@Composable
internal fun DetailsScreen(
    uiState: DetailsUiState,
    onCurrencySelected: (Currency) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(colorScheme.background)
    ) {
        when (uiState) {
            DetailsUiState.Loading -> ArcDetailsListLoading()
            is DetailsUiState.Success -> DetailsScreen(uiState.detailsUiModel, onCurrencySelected)
        }
    }
}

@Composable
private fun DetailsScreen(
    uiModel: CoinDetailsUiModel,
    onCurrencySelected: (Currency) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        DetailsHeader(uiModel)
        ArcSwitcher(
            items = uiModel.marketDataList,
            selectedItem = uiModel.selectedMarketData,
            itemTitle = { it.currencyTitle },
            onItemSelected = { onCurrencySelected(it.currency) },
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        MarketDataContent(uiModel.selectedMarketData)
    }
}

@Composable
private fun DetailsHeader(uiModel: CoinDetailsUiModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        ArcBitcoinIcon()
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = uiModel.name,
                style = typography.titleLarge,
                fontWeight = Bold,
                color = colorScheme.onBackground,
                modifier = Modifier
                    .alignByBaseline()
            )
            Text(
                text = "(${uiModel.symbol})",
                style = typography.labelSmall,
                color = colorScheme.onBackground,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .alignByBaseline(),
            )
        }
        Text(
            text = uiModel.currentPriceDefault,
            style = typography.headlineLarge,
            fontWeight = Bold,
            color = colorScheme.onBackground,
        )
    }
}

@Composable
fun MarketDataContent(uiModel: MarketDataUiModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.title_current_price),
            color = colorScheme.onBackground,
            style = typography.titleMedium,
        )
        Text(
            text = uiModel.currentPrice,
            style = typography.titleLarge,
            color = colorScheme.onBackground,
            fontWeight = Bold,
        )
        Text(
            text = stringResource(R.string.title_market_cap),
            style = typography.titleMedium,
            color = colorScheme.onBackground,
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(
            text = uiModel.marketCap,
            style = typography.titleLarge,
            color = colorScheme.onBackground,
            fontWeight = Bold,
        )
        Text(
            text = stringResource(R.string.title_total_volume),
            style = typography.titleMedium,
            color = colorScheme.onBackground,
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(
            text = uiModel.totalVolume,
            style = typography.titleLarge,
            color = colorScheme.onBackground,
            fontWeight = Bold,
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DetailsScreenPreview() {
    ArcytoTheme {
        val list = listOf(
            MarketDataUiModel(
                currency = Currency.USD,
                currencyTitle = "USD",
                currentPrice = "$130,000",
                marketCap = "$2.6bn",
                totalVolume = "$1.2bn",
            ),
            MarketDataUiModel(
                currency = Currency.EUR,
                currencyTitle = "EUR",
                currentPrice = "EUR 110,000",
                marketCap = "$2.2bn",
                totalVolume = "$1.1bn",
            ),
            MarketDataUiModel(
                currency = Currency.GBP,
                currencyTitle = "GBP",
                currentPrice = "GBP 90,000",
                marketCap = "$1.8bn",
                totalVolume = "$0.9bn",
            ),
        )
        DetailsScreen(
            uiState = DetailsUiState.Success(
                CoinDetailsUiModel(
                    name = "Bitcoin",
                    symbol = "BTC",
                    currentPriceDefault = "$130,000",
                    marketDataList = list,
                    selectedMarketData = list.first(),
                )
            ),
            onCurrencySelected = {},
        )
    }
}

package io.github.msh91.arcyto.details.ui.details

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.msh91.arcyto.core.design.component.ArcCryptoIcon
import io.github.msh91.arcyto.core.design.component.ArcDetailsListLoading
import io.github.msh91.arcyto.core.design.component.ArcSwitcher
import io.github.msh91.arcyto.core.design.theme.ArcytoTheme
import io.github.msh91.arcyto.core.di.viewmodel.arcytoViewModel
import io.github.msh91.arcyto.details.api.navigation.DetailsRouteRequest
import io.github.msh91.arcyto.details.domain.model.CoinDetails
import io.github.msh91.arcyto.details.domain.model.Currency
import io.github.msh91.arcyto.details.impl.R
import io.github.msh91.arcyto.core.design.R as R_design

@Composable
fun DetailsRoute(
    detailsRouteRequest: DetailsRouteRequest,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = arcytoViewModel(),
) {
    LaunchedEffect(detailsRouteRequest) {
        viewModel.fetchCoinDetails(detailsRouteRequest)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetailsScreen(uiState, viewModel::onCurrencySelected, onNavigateBack, modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailsScreen(
    uiState: DetailsUiState,
    onCurrencySelected: (Currency) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        DetailsTopBar(onNavigateBack)
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            when (uiState) {
                DetailsUiState.Loading -> ArcDetailsListLoading()
                is DetailsUiState.Success -> DetailsContent(uiState.detailsUiModel, onCurrencySelected)
                is DetailsUiState.Error -> DetailsScreenError(uiState.message)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DetailsTopBar(onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.title_details_bitcoin),
                color = colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        },
        navigationIcon = {
            IconButton(onNavigateBack) {
                Icon(
                    painter = painterResource(R_design.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = colorScheme.onBackground
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
fun DetailsScreenError(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = message,
            style = typography.titleMedium,
            fontWeight = Bold,
            color = colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun DetailsContent(
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
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            ArcCryptoIcon(imageUrl = uiModel.imageUrl)
            Text(
                text = uiModel.date,
                style = typography.titleMedium,
                color = colorScheme.onBackground,
                fontWeight = Bold,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp),
            )
        }
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
                coinDetails = CoinDetails(
                    id = "btc",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://assets.coingecko.com/coins/images/1/thumb/bitcoin.png",
                    marketDataList = emptyList(),
                ),
                detailsUiModel = CoinDetailsUiModel(
                    name = "Bitcoin",
                    symbol = "BTC",
                    date = "27 Jan 2021",
                    currentPriceDefault = "$130,000",
                    imageUrl = "https://assets.coingecko.com/coins/images/1/thumb/bitcoin.png",
                    marketDataList = list,
                    selectedMarketData = list.first(),
                )
            ),
            onNavigateBack = {},
            onCurrencySelected = {},
        )
    }
}

package io.github.msh91.arcyto.details.ui.details

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.github.msh91.arcyto.core.di.common.CompositeErrorMapper
import io.github.msh91.arcyto.core.formatter.date.DateFormat
import io.github.msh91.arcyto.core.formatter.date.FormatDateUseCase
import io.github.msh91.arcyto.core.formatter.price.FormatPriceUseCase
import io.github.msh91.arcyto.core.tooling.test.rule.MainDispatcherRule
import io.github.msh91.arcyto.details.api.navigation.DetailsRouteRequest
import io.github.msh91.arcyto.details.data.repository.CoinDetailsRepository
import io.github.msh91.arcyto.details.domain.model.CoinDetails
import io.github.msh91.arcyto.details.domain.model.CoinDetailsRequest
import io.github.msh91.arcyto.details.domain.model.Currency
import io.github.msh91.arcyto.details.domain.model.MarketData
import io.github.msh91.arcyto.details.ui.details.DetailsUiState.Error
import io.github.msh91.arcyto.details.ui.details.DetailsUiState.Loading
import io.github.msh91.arcyto.details.ui.details.DetailsUiState.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailsViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainDispatcherRule()
    private lateinit var sut: DetailsViewModel
    private val detailsRepository = mockk<CoinDetailsRepository>()
    private val date = 1738250334000
    private val formattedDate = "30-01-2025"
    private val formatDateUseCase = mockk<FormatDateUseCase> {
        every { this@mockk.invoke(date, DateFormat.DAY_MONTH_YEAR, false) } returns formattedDate
        every { this@mockk.invoke(date, DateFormat.MONTH_DAY, true) } returns "Today"
    }
    private val formatPriceUseCase = mockk<FormatPriceUseCase> {
        every { this@mockk.invoke(1.00, "eur") } returns "€1.00"
        every { this@mockk.invoke(100.00, "eur") } returns "€100.00"
        every { this@mockk.invoke(1000.00, "eur") } returns "€1,000.00"

        every { this@mockk.invoke(2.00, "usd") } returns "$2.00"
        every { this@mockk.invoke(200.00, "usd") } returns "$200.00"
        every { this@mockk.invoke(2000.00, "usd") } returns "$2,000.00"

        every { this@mockk.invoke(3.00, "gbp") } returns "£3.00"
        every { this@mockk.invoke(300.00, "gbp") } returns "£300.00"
        every { this@mockk.invoke(3000.00, "gbp") } returns "£3,000.00"
    }
    private val errorMapper = mockk<CompositeErrorMapper>()

    @Before
    fun setUp() {

        sut = DetailsViewModel(
            detailsRepository = detailsRepository,
            formatPriceUseCase = formatPriceUseCase,
            formatDateUseCase = formatDateUseCase,
            errorMapper = errorMapper,
        )
    }

    @Test
    fun `when viewModel is created then the initial uiState is loading`() = runTest {
        // THEN
        sut.uiState.test {
            assertThat(awaitItem()).isEqualTo(Loading)
        }
    }

    @Test
    fun `fetchCoinDetails - should update the uiState to success`() = runTest {
        // GIVEN

        val coinDetails = getCoinDetails()
        val request = CoinDetailsRequest("bitcoin", formattedDate, false)
        coEvery { detailsRepository.getCoinDetails(request) } returns Result.success(coinDetails)

        sut.uiState.test {
            assertThat(awaitItem()).isEqualTo(Loading)

            // WHEN
            sut.fetchCoinDetails(DetailsRouteRequest("bitcoin", date))

            // THEN
            assertThat(awaitItem()).isEqualTo(Success(getUiModel()))
        }
    }

    @Test
    fun `fetchCoinDetails - should update the uiState to error`() = runTest {
        // GIVEN
        val coinDetails = getCoinDetails()
        val request = CoinDetailsRequest("bitcoin", formattedDate, false)
        val error = mockk<Throwable>()
        val errorMessage = "Something went wrong"
        coEvery { detailsRepository.getCoinDetails(request) } returns Result.failure(error)
        every { errorMapper.getErrorMessage(error) } returns errorMessage

        sut.uiState.test {
            assertThat(awaitItem()).isEqualTo(Loading)

            // WHEN
            sut.fetchCoinDetails(DetailsRouteRequest("bitcoin", date))

            // THEN
            assertThat(awaitItem()).isEqualTo(Error(errorMessage))
        }
    }

    @Test
    fun `onCurrencySelected - selected market data should be updated according to currency`() = runTest {
        // GIVEN
        val coinDetails = getCoinDetails()
        val request = CoinDetailsRequest("bitcoin", formattedDate, false)
        coEvery { detailsRepository.getCoinDetails(request) } returns Result.success(coinDetails)

        sut.uiState.test {
            assertThat(awaitItem()).isEqualTo(Loading)
            sut.fetchCoinDetails(DetailsRouteRequest("bitcoin", date))
            assertThat(awaitItem()).isEqualTo(Success(getUiModel()))

            Currency.entries.asReversed().forEach { currency ->
                // WHEN
                sut.onCurrencySelected(currency)

                // THEN
                assertThat(awaitItem()).isEqualTo(Success(getUiModel(currency)))
            }
        }
    }

    private fun getCoinDetails(): CoinDetails {
        return CoinDetails(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "btc",
            marketDataList = listOf(
                MarketData(
                    currency = Currency.EUR,
                    currentPrice = 1.00,
                    marketCap = 100.00,
                    totalVolume = 1000.00,
                ),
                MarketData(
                    currency = Currency.USD,
                    currentPrice = 2.00,
                    marketCap = 200.00,
                    totalVolume = 2000.00,
                ),
                MarketData(
                    currency = Currency.GBP,
                    currentPrice = 3.00,
                    marketCap = 300.00,
                    totalVolume = 3000.00,
                ),
            )
        )
    }

    private fun getUiModel(defaultCurrency: Currency = Currency.EUR): CoinDetailsUiModel {
        val marketDateList = listOf(
            MarketDataUiModel(
                currency = Currency.EUR,
                currencyTitle = "EUR",
                currentPrice = "€1.00",
                marketCap = "€100.00",
                totalVolume = "€1,000.00",
            ),
            MarketDataUiModel(
                currency = Currency.USD,
                currencyTitle = "USD",
                currentPrice = "$2.00",
                marketCap = "$200.00",
                totalVolume = "$2,000.00",
            ),
            MarketDataUiModel(
                currency = Currency.GBP,
                currencyTitle = "GBP",
                currentPrice = "£3.00",
                marketCap = "£300.00",
                totalVolume = "£3,000.00",
            ),
        )
        return CoinDetailsUiModel(
            name = "Bitcoin",
            symbol = "btc",
            date = "Today",
            currentPriceDefault = "€1.00",
            marketDataList = marketDateList,
            selectedMarketData = marketDateList.first { it.currency == defaultCurrency },
        )
    }
}
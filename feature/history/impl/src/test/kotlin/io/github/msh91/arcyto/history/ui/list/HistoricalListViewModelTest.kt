package io.github.msh91.arcyto.history.ui.list

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.github.msh91.arcyto.core.design.component.PerformanceValue
import io.github.msh91.arcyto.core.di.common.CompositeErrorMapper
import io.github.msh91.arcyto.core.formatter.date.DateFormat
import io.github.msh91.arcyto.core.formatter.date.DateProvider
import io.github.msh91.arcyto.core.formatter.date.FormatDateUseCase
import io.github.msh91.arcyto.core.formatter.price.FormatPriceUseCase
import io.github.msh91.arcyto.core.tooling.test.rule.MainDispatcherRule
import io.github.msh91.arcyto.history.domain.model.HistoricalChartRequest
import io.github.msh91.arcyto.history.domain.model.HistoricalPrice
import io.github.msh91.arcyto.history.domain.model.LatestPrice
import io.github.msh91.arcyto.history.domain.model.LatestPriceRequest
import io.github.msh91.arcyto.history.domain.usecase.GetHistoricalChartUseCase
import io.github.msh91.arcyto.history.domain.usecase.GetLatestPriceUseCase
import io.github.msh91.arcyto.history.ui.list.HistoricalListUiEvent.*
import io.github.msh91.arcyto.history.ui.list.HistoryUiState.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class HistoricalListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val getHistoricalChartUseCase: GetHistoricalChartUseCase = mockk()
    private val getLatestPriceUseCase: GetLatestPriceUseCase = mockk()
    private val errorMapper: CompositeErrorMapper = mockk()
    private val formatDateUseCase: FormatDateUseCase = mockk {
        every { this@mockk.invoke(100, DateFormat.MONTH_DAY, true) } returns "100"
        every { this@mockk.invoke(200, DateFormat.MONTH_DAY, true) } returns "200"
        every { this@mockk.invoke(300, DateFormat.MONTH_DAY, true) } returns "300"
        every { this@mockk.invoke(400, DateFormat.MONTH_DAY, true) } returns "400"
        every { this@mockk.invoke(500, DateFormat.MONTH_DAY, true) } returns "500"
        every { this@mockk.invoke(600, DateFormat.MONTH_DAY, true) } returns "600"
    }
    private val formatPriceUseCase: FormatPriceUseCase = mockk {
        every { this@mockk.invoke(1000.0, "eur") } returns "1000.00"
        every { this@mockk.invoke(2000.0, "eur") } returns "2000.00"
        every { this@mockk.invoke(3000.0, "eur") } returns "3000.00"
        every { this@mockk.invoke(4000.0, "eur") } returns "4000.00"
        every { this@mockk.invoke(5000.0, "eur") } returns "5000.00"
        every { this@mockk.invoke(6000.0, "eur") } returns "6000.00"
    }
    private val dateProvider: DateProvider = mockk {
        every { getCurrentDate() } returns 100
    }
    private lateinit var sut: HistoricalListViewModel

    private val testDispatcher = UnconfinedTestDispatcher()
    private val config = HistoricalListConfig()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Mock formatters
        every { formatDateUseCase.invoke(any(), DateFormat.MONTH_DAY, true) } returns "Jan 1"
        every { formatPriceUseCase.invoke(any(), config.currency) } returns "€1,000.00"
        every { dateProvider.getCurrentDate() } returns 1672531200000 // 2023-01-01
        every { errorMapper.getErrorMessage(any()) } returns "Error message"
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify initial state is Loading`() = runTest {
        // Given
        setupSuccessResponse()

        // When
        sut = createViewModel()

        // Then
        assertThat(sut.uiState.value).isEqualTo(Loading)
    }

    @Test
    fun `verify state is Success with data when both sources succeed`() = runTest {
        // Given
        setupSuccessResponse()

        // When
        sut = createViewModel()

        // Then
        sut.uiState.test {
            // Skip Loading state
            if (awaitItem() is Loading) {
                val successState = awaitItem() as Success

                // Verify current price
                assertThat(successState.currentPriceUiModel?.value).isEqualTo("€1,000.00")
                assertThat(successState.currentPriceUiModel?.date).isEqualTo(1672531200000)

                // Verify historical list
                assertThat(successState.historicalValueUiModels.size).isEqualTo(2)
                assertThat(successState.historicalValueUiModels[0].value).isEqualTo("€1,000.00")
            }


            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `verify performance value is correctly mapped`() = runTest {
        // Given
        setupSuccessResponse()

        // When
        sut = createViewModel()

        // Then
        sut.uiState.test {
            // Skip Loading state
            if (awaitItem() is Loading) {
                val successState = awaitItem() as Success

                // Verify positive performance
                val performanceValue = successState.currentPriceUiModel?.performanceValue
                assertThat(performanceValue?.text).isEqualTo("5.20%")
                assertThat(performanceValue?.isPositive).isTrue()

                // Verify negative performance
                val negativePerformance = successState.historicalValueUiModels[1].performanceValue
                assertThat(negativePerformance?.text).isEqualTo("3.10%")
                assertThat(negativePerformance?.isPositive).isFalse()
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `verify error handling when historical data fails`() = runTest {
        // Given
        val error = IOException("Network error")
        setupHistoricalErrorResponse(error)
        setupLatestPriceSuccessResponse()

        // When
        sut = createViewModel()

        // Then
        sut.uiState.test {
            // Skip Loading state
            if (awaitItem() is Loading) {
                val successState = awaitItem() as Success

                // Verify we still have latest price
                assertThat(successState.currentPriceUiModel?.value).isEqualTo("€1,000.00")

                // Verify historical list is empty
                assertThat(successState.historicalValueUiModels.size).isEqualTo(0)
            }

            cancelAndIgnoreRemainingEvents()
        }

        sut.events.test {
            assertThat(awaitItem()).isEqualTo(ShowSnackbar("Error message"))

            cancelAndIgnoreRemainingEvents()
        }

        verify { errorMapper.getErrorMessage(error) }
    }

    @Test
    fun `verify error handling when latest price fails`() = runTest {
        // Given
        val error = IOException("Network error")
        setupHistoricalSuccessResponse()
        setupLatestPriceErrorResponse(error)

        // When
        sut = createViewModel()

        // Then
        sut.uiState.test {
            // Skip Loading state
            if (awaitItem() is Loading) {
                val successState = awaitItem() as Success

                // Verify latest price is null
                assertThat(successState.currentPriceUiModel).isNull()

                // Verify historical list is present
                assertThat(successState.historicalValueUiModels.size).isEqualTo(2)
            }

            cancelAndIgnoreRemainingEvents()
        }

        sut.events.test {
            assertThat(awaitItem()).isEqualTo(ShowSnackbar("Error message"))

            cancelAndIgnoreRemainingEvents()
        }

        verify { errorMapper.getErrorMessage(error) }
    }

    @Test
    fun `verify navigation event on item click`() = runTest {
        // Given
        setupSuccessResponse()
        sut = createViewModel()

        val item = PriceValueUiModel(
            date = 1640995200000, // 2022-01-01
            formattedDate = "Jan 1",
            value = "€1,000.00",
            performanceValue = PerformanceValue("5.20%", true)
        )

        // When
        sut.onItemClick(item)

        // Then
        sut.events.test {
            val event = awaitItem() as NavigateToDetails
            assertThat(event.detailsRouteRequest.coinId).isEqualTo(config.coinId)
            assertThat(event.detailsRouteRequest.date).isEqualTo(1640995200000)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel(): HistoricalListViewModel = HistoricalListViewModel(
        getHistoricalChartUseCase = getHistoricalChartUseCase,
        getLatestPriceUseCase = getLatestPriceUseCase,
        errorMapper = errorMapper,
        formatDateUseCase = formatDateUseCase,
        formatPriceUseCase = formatPriceUseCase,
        dateProvider = dateProvider
    )

    private fun setupSuccessResponse() {
        setupHistoricalSuccessResponse()
        setupLatestPriceSuccessResponse()
    }

    private fun setupHistoricalSuccessResponse() {
        val historicalPrices = listOf(
            HistoricalPrice(
                date = 1640908800000, // 2021-12-31
                value = 1000.0,
                changePercentage = 5.2
            ),
            HistoricalPrice(
                date = 1640822400000, // 2021-12-30
                value = 950.0,
                changePercentage = -3.1
            )
        )

        coEvery {
            getHistoricalChartUseCase.invoke(
                match<HistoricalChartRequest> {
                    it.id == config.coinId &&
                            it.currency == config.currency &&
                            it.days == config.historicalDays &&
                            it.interval == config.historicalInterval
                }
            )
        } returns Result.success(historicalPrices)
    }

    private fun setupHistoricalErrorResponse(error: Throwable) {
        coEvery {
            getHistoricalChartUseCase.invoke(any())
        } returns Result.failure(error)
    }

    private fun setupLatestPriceSuccessResponse() {
        val latestPrice = LatestPrice(
            coinId = "btc",
            price = 1000.0,
            changePercentage = 5.2,
            lastUpdated = 0,
        )

        every {
            getLatestPriceUseCase.invoke(
                match<LatestPriceRequest> {
                    it.coinId == config.coinId &&
                            it.currency == config.currency
                }
            )
        } returns flowOf(Result.success(latestPrice))
    }

    private fun setupLatestPriceErrorResponse(error: Throwable) {
        every {
            getLatestPriceUseCase.invoke(any())
        } returns flowOf(Result.failure(error))
    }
}


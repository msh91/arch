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
import io.github.msh91.arcyto.details.api.navigation.DetailsRouteRequest
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

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

    @Test
    fun `observeLatestPrice - latest price should be fetched and displayed`() = runTest {
        // GIVEN
        mockSuccessLatestPriceRequest()
        mockSuccessHistoricalChartRequest()

        // WHEN
        sut = createViewModel()

        // THEN
        val expected = getLatestPriceUiModel()

        sut.uiState
            .filterIsInstance<Success>()
            .map { it.currentPriceUiModel }
            .filterNotNull()
            .distinctUntilChanged()
            .test {
                assertThat(awaitItem()).isEqualTo(expected)
            }
    }

    @Test
    fun `observeHistoricalChart - historical chart should be fetched and displayed`() = runTest {
        // GIVEN
        mockSuccessLatestPriceRequest()
        mockSuccessHistoricalChartRequest()

        // WHEN
        sut = createViewModel()

        // THEN
        val expected = getHistoricalPriceUiModels()

        sut.uiState
            .filterIsInstance<Success>()
            .map { it.historicalValueUiModels }
            .filterNotNull()
            .distinctUntilChanged()
            .test {
                assertThat(awaitItem()).isEqualTo(expected)
            }
    }

    @Test
    fun `observeHistoricalChart - error should be displayed`() = runTest {
        // GIVEN
        mockSuccessLatestPriceRequest()
        val throwable = mockk<Throwable>()
        coEvery { getHistoricalChartUseCase.invoke(any()) } returns Result.failure(throwable)
        every { errorMapper.getErrorMessage(throwable) } returns "Error"

        // WHEN
        sut = createViewModel()

        // THEN
        sut.events.test {
            assertThat(awaitItem()).isEqualTo(ShowSnackbar("Error"))
        }
    }

    @Test
    fun `observeLatestPrice - error should be displayed`() = runTest {
        // GIVEN
        mockSuccessHistoricalChartRequest()
        val throwable = mockk<Throwable>()
        coEvery { getLatestPriceUseCase.invoke(any()) } returns flow {
            emit(Result.failure(throwable))
        }
        every { errorMapper.getErrorMessage(throwable) } returns "Error"

        // WHEN
        sut = createViewModel()

        // THEN
        sut.events.test {
            assertThat(awaitItem()).isEqualTo(ShowSnackbar("Error"))
        }
    }

    @Test
    fun `observeHistoricalChart - today prices should be removed from historical list`() = runTest {
        // GIVEN
        mockSuccessLatestPriceRequest()
        mockSuccessHistoricalChartRequest(withTodayPrice = true)

        // WHEN
        sut = createViewModel()

        // THEN
        sut.uiState
            .filterIsInstance<Success>()
            .map { it.historicalValueUiModels }
            .filterNotNull()
            .distinctUntilChanged()
            .test {
                assertThat(awaitItem()).isEqualTo(getHistoricalPriceUiModels())
            }
    }

    @Test
    fun `onItemClick - navigate to detail screen`() = runTest {
        // GIVEN
        mockSuccessLatestPriceRequest()
        mockSuccessHistoricalChartRequest()

        // WHEN
        sut = createViewModel()
        sut.onItemClick(getHistoricalPriceUiModels().first())

        // THEN
        sut.events.test {
            assertThat(awaitItem()).isEqualTo(NavigateToDetails(DetailsRouteRequest("bitcoin", 400)))
        }
    }

    private fun mockSuccessLatestPriceRequest() {
        val price = getLatestPrices()
        val request = LatestPriceRequest(
            coinId = "bitcoin",
            currency = "eur",
            precision = 2,
            intervalMs = 60_000
        )
        coEvery { getLatestPriceUseCase.invoke(request) } returns flow {
            emit(Result.success(price))
        }
    }

    private fun mockSuccessHistoricalChartRequest(withTodayPrice: Boolean = false) {
        val request = HistoricalChartRequest(
            id = "bitcoin",
            currency = "eur",
            days = 15,
            interval = "daily",
            precision = 2,
        )
        coEvery { getHistoricalChartUseCase.invoke(request) } returns Result.success(getHistoricalPrices(withTodayPrice))
    }

    private fun getHistoricalPrices(withTodayPrice: Boolean = false): List<HistoricalPrice> = buildList {
        if (withTodayPrice) {
            add(HistoricalPrice(date = System.currentTimeMillis(), value = 1000.0, changePercentage = 10.234))
        }
        add(HistoricalPrice(date = 400, value = 4000.0, changePercentage = 10.234))
        add(HistoricalPrice(date = 500, value = 5000.0, changePercentage = 20.1))
        add(HistoricalPrice(date = 600, value = 6000.0, changePercentage = -30.0))
    }

    private fun getHistoricalPriceUiModels() = listOf(
        PriceValueUiModel(
            date = 400,
            formattedDate = "400",
            value = "4000.00",
            performanceValue = PerformanceValue(text = "10.23%", isPositive = true)
        ),
        PriceValueUiModel(
            date = 500,
            formattedDate = "500",
            value = "5000.00",
            performanceValue = PerformanceValue(text = "20.10%", isPositive = true)
        ),
        PriceValueUiModel(
            date = 600,
            formattedDate = "600",
            value = "6000.00",
            performanceValue = PerformanceValue(text = "30.00%", isPositive = false)
        )
    )

    private fun getLatestPrices() = LatestPrice(
        coinId = "bitcoin",
        price = 1000.0,
        lastUpdated = 100,
        changePercentage = 10.234
    )

    private fun getLatestPriceUiModel() = PriceValueUiModel(
        date = 100,
        formattedDate = "100",
        value = "1000.00",
        performanceValue = PerformanceValue(text = "10.23%", isPositive = true)
    )

    private fun createViewModel(): HistoricalListViewModel = HistoricalListViewModel(
        getHistoricalChartUseCase = getHistoricalChartUseCase,
        getLatestPriceUseCase = getLatestPriceUseCase,
        errorMapper = errorMapper,
        formatDateUseCase = formatDateUseCase,
        formatPriceUseCase = formatPriceUseCase,
        dateProvider = dateProvider,
    )
}


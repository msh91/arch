package io.github.msh91.arcyto.history.data.repository

import com.google.common.truth.Truth.assertThat
import io.github.msh91.arcyto.history.data.remote.HistoricalRemoteDataSource
import io.github.msh91.arcyto.history.data.remote.model.HistoricalChartApiModel
import io.github.msh91.arcyto.history.data.remote.model.LatestPriceApiModel
import io.github.msh91.arcyto.history.domain.model.HistoricalChart
import io.github.msh91.arcyto.history.domain.model.HistoricalChartRequest
import io.github.msh91.arcyto.history.domain.model.ChartPrice
import io.github.msh91.arcyto.history.domain.model.LatestPrice
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class HistoricalChartRepositoryImplTest {
    private val dataSource: HistoricalRemoteDataSource = mockk()
    private lateinit var sut: HistoricalChartRepositoryImpl

    @Before
    fun setUp() {
        sut = HistoricalChartRepositoryImpl(dataSource)
    }

    @Test
    fun `getHistoricalChart should return historical chart`() = runTest {
        // WHEN
        val request = HistoricalChartRequest(
            id = "bitcoin",
            currency = "EUR",
            days = 30,
            interval = "daily",
            precision = 2,
        )
        coEvery {
            dataSource.getHistoricalChart(
                request.id,
                request.currency,
                request.days,
                request.interval,
                request.precision
            )
        } returns Result.success(getApiModel())

        // WHEN
        val result = sut.getHistoricalChart(request)

        // THEN
        assertThat(result).isEqualTo(Result.success(getHistoricalChart()))
    }

    @Test
    fun `getLatestCoinPrice should return latest coin price`() = runTest {
        // GIVEN
        val apiModel = mapOf(
            "bitcoin" to LatestPriceApiModel(
                priceInEur = 1000.0,
                lastUpdatedAt = 100,
                changePercentage = 10.0,
            )
        )
        coEvery {
            dataSource.getLatestCoinPrice("bitcoin", "eur", 2)
        } returns Result.success(apiModel)

        // WHEN
        val result = sut.getLatestCoinPrice("bitcoin", "eur", 2)

        // THEN
        val expected = LatestPrice(
            coinId = "bitcoin",
            price = 1000.0,
            lastUpdated = 100,
            changePercentage = 10.0
        )
        assertThat(result).isEqualTo(Result.success(expected))
    }

    private fun getHistoricalChart() = HistoricalChart(
        chartPrices = listOf(
            ChartPrice(100, 1000.0),
            ChartPrice(200, 2000.0),
            ChartPrice(300, 3000.0),
        )
    )

    private fun getApiModel() = HistoricalChartApiModel(
        prices = listOf(
            listOf(100.0, 1000.0),
            listOf(200.0, 2000.0),
            listOf(300.0, 3000.0),
        ),
    )
}

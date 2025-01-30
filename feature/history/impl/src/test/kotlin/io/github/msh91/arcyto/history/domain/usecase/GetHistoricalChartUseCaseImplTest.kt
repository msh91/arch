package io.github.msh91.arcyto.history.domain.usecase

import com.google.common.truth.Truth.assertThat
import io.github.msh91.arcyto.history.data.repository.HistoricalChartRepository
import io.github.msh91.arcyto.history.domain.model.ChartPrice
import io.github.msh91.arcyto.history.domain.model.HistoricalChart
import io.github.msh91.arcyto.history.domain.model.HistoricalChartRequest
import io.github.msh91.arcyto.history.domain.model.HistoricalPrice
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetHistoricalChartUseCaseImplTest {
    private val repository = mockk<HistoricalChartRepository>()
    private lateinit var sut: GetHistoricalChartUseCase

    @Before
    fun setUp() {
        sut = GetHistoricalChartUseCaseImpl(repository)
    }

    @Test
    fun `invoke should return list of prices sorted by date descending`() = runTest {
        // GIVEN
        val historicalChart = HistoricalChart(
            chartPrices = listOf(
                ChartPrice(date = 2, value = 1000.0),
                ChartPrice(date = 1, value = 2000.0),
                ChartPrice(date = 3, value = 1500.0),
            )
        )
        val request = HistoricalChartRequest(
            id = "bitcoin",
            currency = "EUR",
            days = 30,
            interval = "daily",
            precision = 2
        )
        coEvery { repository.getHistoricalChart(request) } returns Result.success(historicalChart)

        // WHEN
        val result = sut.invoke(request)

        // THEN
        val expected = listOf(
            HistoricalPrice(date = 3, value = 1500.0, changePercentage = 50.0),
            HistoricalPrice(date = 2, value = 1000.0, changePercentage = -50.0),
            HistoricalPrice(date = 1, value = 2000.0, changePercentage = null),
        )
        assertThat(result).isEqualTo(Result.success(expected))
    }
}
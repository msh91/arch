package io.github.msh91.arcyto.history.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.github.msh91.arcyto.history.data.repository.HistoricalChartRepository
import io.github.msh91.arcyto.history.domain.model.LatestPrice
import io.github.msh91.arcyto.history.domain.model.LatestPriceRequest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetLatestPriceUseCaseImplTest {
    private val repository = mockk<HistoricalChartRepository>()
    private lateinit var sut: GetLatestPriceUseCaseImpl

    @Before
    fun setUp() {
        sut = GetLatestPriceUseCaseImpl(repository)
    }

    @Test
    fun `invoke should return the latest price every 60 seconds`() = runTest {
        // GIVEN
        val prices = getPrices().map { Result.success(it) }
        val request = LatestPriceRequest(
            coinId = "bitcoin",
            currency = "EUR",
            precision = 2,
            intervalMs = 100,
        )
        coEvery {
            repository.getLatestCoinPrice(
                request.coinId,
                request.currency,
                request.precision
            )
        } returnsMany prices

        // WHEN
        val flow = sut.invoke(request)
        flow.test {
            assertThat(awaitItem()).isEqualTo(prices[0])
            assertThat(awaitItem()).isEqualTo(prices[1])
            assertThat(awaitItem()).isEqualTo(prices[2])
        }
    }

    private fun getPrices(): List<LatestPrice> = listOf(
        LatestPrice(
            coinId = "bitcoin",
            price = 1000.0,
            lastUpdated = 100,
            changePercentage = 10.0,
        ),
        LatestPrice(
            coinId = "bitcoin",
            price = 2000.0,
            lastUpdated = 200,
            changePercentage = 20.0,
        ),
        LatestPrice(
            coinId = "bitcoin",
            price = 3000.0,
            lastUpdated = 300,
            changePercentage = 30.0,
        ),
    )
}
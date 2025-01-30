package io.github.msh91.arcyto.details.data.repository

import com.google.common.truth.Truth.assertThat
import io.github.msh91.arcyto.details.data.remote.CoinDetailsDataSource
import io.github.msh91.arcyto.details.data.remote.model.CoinDetailsApiModel
import io.github.msh91.arcyto.details.data.remote.model.MarketDataApiModel
import io.github.msh91.arcyto.details.domain.model.CoinDetails
import io.github.msh91.arcyto.details.domain.model.CoinDetailsRequest
import io.github.msh91.arcyto.details.domain.model.Currency
import io.github.msh91.arcyto.details.domain.model.MarketData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CoinDetailsRepositoryImplTest {
    private val dataSource: CoinDetailsDataSource = mockk()
    private lateinit var sut: CoinDetailsRepositoryImpl

    @Before
    fun setUp() {
        sut = CoinDetailsRepositoryImpl(dataSource)
    }

    @Test
    fun `should get coin details`() = runTest {
        // GIVEN
        val request = CoinDetailsRequest("id", "date", false)
        coEvery { dataSource.getCoinDetails("id", "date", false) } returns Result.success(detailsApiModel)

        // WHEN
        val result = sut.getCoinDetails(request)

        // THEN
        assertThat(result).isEqualTo(Result.success(coinDetails))
    }

    private val detailsApiModel = CoinDetailsApiModel(
        id = "bitcoin",
        name = "Bitcoin",
        symbol = "btc",
        marketDataApiModel = MarketDataApiModel(
            currentPrice = mapOf("eur" to 1.0, "usd" to 2.0, "gbp" to 3.0),
            marketCap = mapOf("eur" to 10.0, "usd" to 20.0, "gbp" to 30.0),
            totalVolume = mapOf("eur" to 100.0, "usd" to 200.0, "gbp" to 300.0),
        ),
    )
    private val coinDetails = CoinDetails(
        id = "bitcoin",
        name = "Bitcoin",
        symbol = "btc",
        marketDataList = listOf(
            MarketData(Currency.EUR, 1.0, 10.0, 100.0),
            MarketData(Currency.USD, 2.0, 20.0, 200.0),
            MarketData(Currency.GBP, 3.0, 30.0, 300.0),
        ),
    )
}
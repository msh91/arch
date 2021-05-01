package io.github.msh91.arch.ui.home.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import arrow.core.Either
import io.github.msh91.arch.CoroutinesTestRule
import io.github.msh91.arch.R
import io.github.msh91.arch.data.model.Error
import io.github.msh91.arch.data.model.HttpError
import io.github.msh91.arch.data.model.crypto.CryptoCurrency
import io.github.msh91.arch.data.model.crypto.CurrencyQuote
import io.github.msh91.arch.data.model.crypto.QuoteKey
import io.github.msh91.arch.data.repository.crypto.CryptoRepository
import io.github.msh91.arch.util.providers.BaseResourceProvider
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeListViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutinesTestRule()

    @MockK
    lateinit var resourceProvider: BaseResourceProvider

    @MockK
    lateinit var cryptoRepository: CryptoRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getLatestUpdates should be called by creation of viewModel`() {
        // GIVEN
        coEvery { cryptoRepository.getLatestUpdates() } returns mockk()

        // WHEN
        val homeListViewModel = HomeListViewModel(cryptoRepository, resourceProvider)

        // THEN
        coVerify { cryptoRepository.getLatestUpdates() }
    }

    @Test
    fun `relevant error message should be shown when receiving latest updates failed`() {
        // GIVEN
        val testObserver = mockk<Observer<String>>()
        val mockedError = mockk<Error>()
        val errorMessage = "Error message"
        every { testObserver.onChanged(any()) } answers {}
        every { resourceProvider.getErrorMessage(mockedError) } returns errorMessage
        coEvery { cryptoRepository.getLatestUpdates() } returns Either.left(mockedError)

        // WHEN
        val homeListViewModel = HomeListViewModel(cryptoRepository, resourceProvider)
        homeListViewModel.errorLiveData.observeForever(testObserver)

        // THEN
        verify { testObserver.onChanged(errorMessage) }
    }

    @Test
    fun `latest crypto updates should be provided to be shown by the view`() {
        // GIVEN
        val testObserver = mockk<Observer<List<CryptoCurrencyItem>>>()
        every { testObserver.onChanged(any()) } answers {}
        val mockedQuote = mockk<CurrencyQuote>()
        val mockedCrypto = mockk<CryptoCurrency>()
        every { mockedCrypto.name } returns "Bitcoin"
        every { mockedCrypto.quotes } returns mapOf(QuoteKey.USD to mockedQuote)
        every { mockedQuote.price } returns 1000.0
        every { mockedQuote.percentChange24h } returns 10.0
        coEvery { cryptoRepository.getLatestUpdates() } returns Either.right(listOf(mockedCrypto))
        every { resourceProvider.getString(R.string.holder_usd_price, 1000.0) } returns "1000 $"
        every { resourceProvider.getString(R.string.holder_percent, 10.0) } returns "10%"
        every { resourceProvider.getColor(any()) } returns 1

        // WHEN
        val homeListViewModel = HomeListViewModel(cryptoRepository, resourceProvider)
        homeListViewModel.cryptoCurrencies.observeForever(testObserver)

        // THEN
        verify { resourceProvider.getColor(R.color.green) }
        verify { testObserver.onChanged(listOf(CryptoCurrencyItem("Bitcoin", "1000 $", "10%", 1))) }
    }

    @Test
    fun `if a USD quote is not exists then an InvalidResponse should be shown`() {
        // GIVEN
        val testObserver = mockk<Observer<String>>()
        every { testObserver.onChanged(any()) } answers {}
        val mockedQuote = mockk<CurrencyQuote>()
        val mockedCrypto = mockk<CryptoCurrency>()
        every { mockedCrypto.quotes } returns mapOf(QuoteKey.BTC to mockedQuote)
        every { resourceProvider.getString(R.string.error_invalid_quote) } returns "invalid quote!"
        every { resourceProvider.getErrorMessage(any()) } returns "invalid quote!"
        coEvery { cryptoRepository.getLatestUpdates() } returns Either.right(listOf(mockedCrypto))

        // WHEN
        val homeListViewModel = HomeListViewModel(cryptoRepository, resourceProvider)
        homeListViewModel.errorLiveData.observeForever(testObserver)

        // THEN
        verify { resourceProvider.getErrorMessage(HttpError.InvalidResponse(100, "invalid quote!")) }
        verify { testObserver.onChanged("invalid quote!") }
    }
}

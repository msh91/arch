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
import io.github.msh91.arch.util.provider.BaseResourceProvider
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LatestUpdatesViewModelTest {
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
    fun `getLatestUpdates should be called after view started`() {
        // GIVEN
        coEvery { cryptoRepository.getLatestUpdates() } returns mockk()

        // WHEN
        val viewModel = LatestUpdatesViewModel(cryptoRepository, resourceProvider)
        viewModel.onStart()

        // THEN
        coVerify { cryptoRepository.getLatestUpdates() }
    }

    @Test
    fun `getLatestUpdates should not be called after view restarted`() {
        // GIVEN
        coEvery { cryptoRepository.getLatestUpdates() } returns mockk()

        // WHEN
        val viewModel = LatestUpdatesViewModel(cryptoRepository, resourceProvider)
        viewModel.onStart()
        viewModel.onStart()

        // THEN
        coVerify(exactly = 1) { cryptoRepository.getLatestUpdates() }
    }

    @Test
    fun `loading should be set to true before calling api and then after api call should be set to false`() {
        val testObserver = mockk<Observer<Boolean>>()
        every { testObserver.onChanged(any()) } answers {}
        coEvery { cryptoRepository.getLatestUpdates() } returns mockk()

        // WHEN
        val viewModel = LatestUpdatesViewModel(cryptoRepository, resourceProvider)
        viewModel.loadingLiveData.observeForever(testObserver)
        viewModel.onStart()

        // THEN
        coVerifyOrder {
            testObserver.onChanged(true)
            cryptoRepository.getLatestUpdates()
            testObserver.onChanged(false)
        }
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
        val viewModel = LatestUpdatesViewModel(cryptoRepository, resourceProvider)
        viewModel.errorLiveData.observeForever(testObserver)
        viewModel.onStart()

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
        val viewModel = LatestUpdatesViewModel(cryptoRepository, resourceProvider)
        viewModel.cryptoCurrencyItemsLiveData.observeForever(testObserver)
        viewModel.onStart()

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
        val viewModel = LatestUpdatesViewModel(cryptoRepository, resourceProvider)
        viewModel.errorLiveData.observeForever(testObserver)
        viewModel.onStart()

        // THEN
        verify { resourceProvider.getErrorMessage(HttpError.InvalidResponse(100, "invalid quote!")) }
        verify { testObserver.onChanged("invalid quote!") }
    }
}

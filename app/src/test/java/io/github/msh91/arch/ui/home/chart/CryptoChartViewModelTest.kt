package io.github.msh91.arch.ui.home.chart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import arrow.core.left
import arrow.core.right
import io.github.msh91.arch.CoroutinesTestRule
import io.github.msh91.arch.R
import io.github.msh91.arch.data.model.Error
import io.github.msh91.arch.data.model.crypto.ChartValue
import io.github.msh91.arch.data.model.crypto.CryptoChartInfo
import io.github.msh91.arch.data.repository.crypto.CryptoChartRepository
import io.github.msh91.arch.util.provider.BaseResourceProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@ExperimentalCoroutinesApi
class CryptoChartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutinesTestRule()

    @MockK
    lateinit var resourceProvider: BaseResourceProvider

    @MockK
    lateinit var cryptoChartRepository: CryptoChartRepository

    private lateinit var viewModel: CryptoChartViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = CryptoChartViewModel(cryptoChartRepository, resourceProvider)
    }

    @Test
    fun `getChartInfo should be called after view started`() {
        // GIVEN
        coEvery { cryptoChartRepository.getChartInfo() } returns mockk()

        // WHEN

        viewModel.onStart()

        // THEN
        coVerify { cryptoChartRepository.getChartInfo() }
    }

    @Test
    fun `getChartInfo should not be called after view re-started`() {
        // GIVEN
        coEvery { cryptoChartRepository.getChartInfo() } returns mockk()

        // WHEN

        viewModel.onStart()
        viewModel.onStart()

        // THEN
        coVerify(exactly = 1) { cryptoChartRepository.getChartInfo() }
    }

    @Test
    fun `chartInfo loading should be set correctly based on api call`() {
        // GIVEN
        val testObserver = mockk<Observer<Boolean>>()
        every { testObserver.onChanged(any()) } answers {}
        coEvery { cryptoChartRepository.getChartInfo() } returns mockk()
        viewModel.loadingLiveData.observeForever(testObserver)

        // WHEN
        viewModel.onStart()

        // THEN
        coVerifyOrder {
            testObserver.onChanged(true)
            cryptoChartRepository.getChartInfo()
            testObserver.onChanged(false)
        }
    }

    @Test
    fun `relevant error message should be shown when receiving chart info failed`() {
        // GIVEN
        val testObserver = mockk<Observer<String>>()
        val mockedError = mockk<Error>()
        val errorMessage = "Error message"
        every { testObserver.onChanged(any()) } answers {}
        every { resourceProvider.getErrorMessage(mockedError) } returns errorMessage
        coEvery { cryptoChartRepository.getChartInfo() } returns mockedError.left()
        viewModel.errorLiveData.observeForever(testObserver)

        // WHEN
        viewModel.onStart()

        // THEN
        verify { testObserver.onChanged(errorMessage) }
    }

    @Test
    fun `chartInfoItem should be provided to be shown by the view`() {
        // GIVEN
        val testObserver = mockk<Observer<ChartInfoItem>>()
        every { testObserver.onChanged(any()) } answers {}
        viewModel.chartInfoLiveData.observeForever(testObserver)
        val mockedInfo = mockk<CryptoChartInfo>()
        every { mockedInfo.name } returns "name"
        every { mockedInfo.description } returns "description"
        every { mockedInfo.chartValues } returns listOf(
            ChartValue(Date(2021, 0, 10), 50_000f),
            ChartValue(Date(2021, 1, 13), 60_000f),
            ChartValue(Date(2021, 2, 11), 40_000f),
            ChartValue(Date(2021, 3, 1), 70_000f)
        )
        every { resourceProvider.getString(R.string.holder_day_month, "Jan", 10) } returns "Jan 10"
        every { resourceProvider.getString(R.string.holder_day_month, "Feb", 13) } returns "Feb 13"
        every { resourceProvider.getString(R.string.holder_day_month, "Mar", 11) } returns "Mar 11"
        every { resourceProvider.getString(R.string.holder_day_month, "Apr", 1) } returns "Apr 1"
        coEvery { cryptoChartRepository.getChartInfo() } returns mockedInfo.right()

        // WHEN
        viewModel.onStart()

        // THEN
        val item = ChartInfoItem(
            "name", "description", listOf(
                ChartEntry("Jan 10", 50000f),
                ChartEntry("Feb 13", 60000f),
                ChartEntry("Mar 11", 40000f),
                ChartEntry("Apr 1", 70000f)
            )
        )
        verify { testObserver.onChanged(item) }
    }
}

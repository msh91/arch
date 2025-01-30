package io.github.msh91.arcyto.core.formatter.date

import com.google.common.truth.Truth.assertThat
import io.github.msh91.arcyto.core.data.local.resource.StringProvider
import io.github.msh91.arcyto.core.formatter.R
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class FormatDateUseCaseImplTest {
    private val stringProvider = mockk<StringProvider>()
    private val dateProvider = mockk<DateProvider>()

    private lateinit var sut: FormatDateUseCaseImpl
    private val today = Calendar.getInstance().apply {
        // set the date to Jan 30, 2025
        timeInMillis = 1738250334000
    }

    private val yesterday = Calendar.getInstance().apply {
        // set the date to Jan 29, 2025
        timeInMillis = 1738163934000
    }

    @Before
    fun setUp() {
        every { dateProvider.getCurrentCalendarDate() } answers {
            Calendar.getInstance()
                .apply { timeInMillis = today.timeInMillis }
        }
        sut = FormatDateUseCaseImpl(
            stringProvider = stringProvider,
            dateProvider = dateProvider
        )
    }

    @Test
    fun `when date is today then returns Today`() {
        // GIVEN
        val formattedDate = "Today"
        every { stringProvider.getString(R.string.title_date_today) } returns formattedDate

        // WHEN
        val actual = sut.invoke(today.timeInMillis, DateFormat.DAY_MONTH_YEAR, true)

        // THEN
        assertThat(actual).isEqualTo(formattedDate)
    }

    @Test
    fun `when date is yesterday then returns Yesterday`() {
        // GIVEN
        val formattedDate = "Yesterday"
        every { stringProvider.getString(R.string.title_date_yesterday) } returns formattedDate
        every { stringProvider.getString(R.string.title_date_today) } returns "Today"

        // WHEN
        val actual = sut.invoke(yesterday.timeInMillis, DateFormat.MONTH_DAY, true)

        // THEN
        assertThat(actual).isEqualTo(formattedDate)
    }

    @Test
    fun `when date is not today or yesterday then returns the date in the format MMM dd`() {
        // GIVEN
        val formattedDate = "Jan 27"
        every { stringProvider.getString(R.string.title_date_today) } returns formattedDate
        every { stringProvider.getString(R.string.title_date_yesterday) } returns formattedDate

        // WHEN
        val date = Calendar.getInstance().apply {
            // set the date to Jan 27, 2025
            timeInMillis = 1737951334000
        }
        val actual = sut.invoke(date.timeInMillis, DateFormat.MONTH_DAY, true)

        // THEN
        assertThat(actual).isEqualTo(formattedDate)
    }

    @Test
    fun `when simplified is false then returns the date in the requested format`() {
        // GIVEN
        val expectedToday = "Jan 30"
        val expectedYesterday = "Jan 29"
        every { stringProvider.getString(R.string.title_date_today) } returns expectedToday
        every { stringProvider.getString(R.string.title_date_yesterday) } returns expectedYesterday

        // WHEN
        val actualToday = sut.invoke(today.timeInMillis, DateFormat.MONTH_DAY, false)
        val actualYesterday = sut.invoke(yesterday.timeInMillis, DateFormat.MONTH_DAY, false)

        // THEN
        assertThat(actualToday).isEqualTo(expectedToday)
        assertThat(actualYesterday).isEqualTo(expectedYesterday)
    }

    @Test
    fun `when format is dd-mm-yyyy then returns the date in the requested format`() {
        // GIVEN
        val expectedToday = "30-01-2025"
        val expectedYesterday = "29-01-2025"

        // WHEN
        val actualToday = sut.invoke(today.timeInMillis, DateFormat.DAY_MONTH_YEAR, false)
        val actualYesterday = sut.invoke(yesterday.timeInMillis, DateFormat.DAY_MONTH_YEAR, false)

        // THEN
        assertThat(actualToday).isEqualTo(expectedToday)
        assertThat(actualYesterday).isEqualTo(expectedYesterday)
    }
}
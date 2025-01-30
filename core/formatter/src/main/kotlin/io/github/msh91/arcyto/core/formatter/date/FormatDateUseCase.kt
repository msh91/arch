package io.github.msh91.arcyto.core.formatter.date

import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.data.local.resource.StringProvider
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.core.formatter.R
import io.github.msh91.arcyto.core.tooling.extension.isSameDayAs
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

interface FormatDateUseCase {
    /**
     * Formats the given date into a string representation based on the specified format and
     * simplification flag.
     *
     * @param date The date to be formatted, represented as milliseconds from the epoch.
     * @param format The desired date format either as a [DateFormat] enum.
     * @param simplified If true, returns "Today" or "Yesterday" for current and previous days,
     *                   otherwise returns the date in the specified format.
     * @return A string representation of the formatted date.
     *
     **/
    operator fun invoke(date: Long, format: DateFormat, simplified: Boolean): String

    /**
     * Formats the given date into a string representation based on the specified format and
     * simplification flag.
     *
     * @param date The date to be formatted, represented as milliseconds from the epoch.
     * @param format The desired date format either as custom string pattern.
     * @param simplified If true, returns "Today" or "Yesterday" for current and previous days,
     *                   otherwise returns the date in the specified format.
     * @return A string representation of the formatted date.
     *
     **/
    operator fun invoke(date: Long, format: String, simplified: Boolean): String
}

enum class DateFormat(val value: String) {
    DAY_MONTH_YEAR("dd-MM-yyyy"),
    MONTH_DAY("MMM dd"),
}

/**
 * An implementation of [FormatDateUseCase]. It returns the date as a string with the following format:
 * If the date is today then it returns "Today",
 * If the date is yesterday then it returns "Yesterday",
 * otherwise it returns the date in the format "MMM dd" (For example: "Jan 27").
 */
@ContributesBinding(AppScope::class)
class FormatDateUseCaseImpl @Inject constructor(
    private val stringProvider: StringProvider,
    private val dateProvider: DateProvider,
) : FormatDateUseCase {

    override fun invoke(date: Long, format: DateFormat, simplified: Boolean): String {
        return invoke(date, format.value, simplified)
    }

    override fun invoke(date: Long, format: String, simplified: Boolean): String {
        val calendarDate = dateProvider.getCurrentCalendarDate().apply { timeInMillis = date }
        val today = dateProvider.getCurrentCalendarDate()
        val yesterday = dateProvider.getCurrentCalendarDate().apply { add(Calendar.DAY_OF_YEAR, -1) }
        return when {
            simplified && calendarDate.isSameDayAs(today) -> stringProvider.getString(R.string.title_date_today)
            simplified && calendarDate.isSameDayAs(yesterday) -> stringProvider.getString(R.string.title_date_yesterday)
            else -> SimpleDateFormat(format, Locale.getDefault()).format(date)
        }
    }
}
package io.github.msh91.arcyto.history.domain.usecase

import android.content.Context
import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.history.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

interface FormatDateUseCase {
    fun invoke(date: Long): String
}

/**
 * An implementation of [FormatDateUseCase]. It returns the date as a string with the following format:
 * If the date is today then it returns "Today",
 * If the date is yesterday then it returns "Yesterday",
 * otherwise it returns the date in the format "MMM dd" (For example: "Jan 27").
 */
@ContributesBinding(AppScope::class)
class FormatDateUseCaseImpl @Inject constructor(
    private val context: Context,
) : FormatDateUseCase {
    override fun invoke(date: Long): String {

        val calendarDate = Calendar.getInstance().apply { timeInMillis = date }
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        return when {
            today.timeInMillis - date < RECENTLY_THRESHOLD_MS -> context.getString(R.string.title_date_recently)
            calendarDate.isSameDayAs(today) -> context.getString(R.string.title_date_today)
            calendarDate.isSameDayAs(yesterday) -> context.getString(R.string.title_date_yesterday)
            else -> SimpleDateFormat(MONTH_DAY_FORMAT, Locale.getDefault()).format(date)
        }
    }


    private fun Calendar.isSameDayAs(other: Calendar): Boolean =
        get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
                get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)

    companion object {
        private const val MONTH_DAY_FORMAT = "MMM dd"
        private const val RECENTLY_THRESHOLD_MS = 60_000
    }
}
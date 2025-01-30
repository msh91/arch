package io.github.msh91.arcyto.history.domain.usecase

import android.content.Context
import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.data.local.resource.StringProvider
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.core.tooling.extension.isSameDayAs
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
    private val stringProvider: StringProvider,
) : FormatDateUseCase {
    override fun invoke(date: Long): String {

        val calendarDate = Calendar.getInstance().apply { timeInMillis = date }
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        return when {
            calendarDate.isSameDayAs(today) -> stringProvider.getString(R.string.title_date_today)
            calendarDate.isSameDayAs(yesterday) -> stringProvider.getString(R.string.title_date_yesterday)
            else -> SimpleDateFormat(MONTH_DAY_FORMAT, Locale.getDefault()).format(date)
        }
    }

    companion object {
        private const val MONTH_DAY_FORMAT = "MMM dd"
    }
}
package io.github.msh91.arcyto.core.formatter.date

import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.di.scope.AppScope
import java.util.Calendar
import javax.inject.Inject

interface DateProvider {
    fun getCurrentDate(): Long
    fun getCurrentCalendarDate(): Calendar
}

@ContributesBinding(AppScope::class)
class DateProviderImpl @Inject constructor() : DateProvider {

    override fun getCurrentDate(): Long = getCurrentCalendarDate().timeInMillis

    override fun getCurrentCalendarDate(): Calendar = Calendar.getInstance()
}
package io.github.msh91.arcyto.core.formatter.date

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import java.util.Calendar

interface DateProvider {
    fun getCurrentDate(): Long

    fun getCurrentCalendarDate(): Calendar
}

@Inject
@ContributesBinding(AppScope::class)
class DateProviderImpl : DateProvider {
    override fun getCurrentDate(): Long = getCurrentCalendarDate().timeInMillis

    override fun getCurrentCalendarDate(): Calendar = Calendar.getInstance()
}

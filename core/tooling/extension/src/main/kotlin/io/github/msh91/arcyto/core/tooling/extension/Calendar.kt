package io.github.msh91.arcyto.core.tooling.extension

import java.util.Calendar

fun Calendar.isSameDayAs(other: Calendar): Boolean =
    get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
            get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)

fun Long.isToday(): Boolean =
    Calendar.getInstance().apply { timeInMillis = this@isToday }.isSameDayAs(Calendar.getInstance())
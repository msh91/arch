package io.github.msh91.arch.ui.home.chart

import com.anychart.chart.common.dataentry.ValueDataEntry

data class ChartInfoItem(
    val entries: List<ValueDataEntry>,
    val name: String,
    val description: String
)

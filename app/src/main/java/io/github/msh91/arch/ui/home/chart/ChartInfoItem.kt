package io.github.msh91.arch.ui.home.chart

import com.anychart.chart.common.dataentry.ValueDataEntry

data class ChartInfoItem(
    val name: String,
    val description: String,
    val entries: List<ChartEntry>
)

data class ChartEntry(val x: String, val y: Float)

fun ChartEntry.toDataEntry(): ValueDataEntry {
    return ValueDataEntry(x, y)
}

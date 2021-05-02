package io.github.msh91.arch.ui.home.chart

import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.charts.Cartesian
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import io.github.msh91.arch.R
import io.github.msh91.arch.databinding.FragmentCryptoChartBinding
import io.github.msh91.arch.ui.base.BaseFragment
import io.github.msh91.arch.ui.base.ViewModelScope
import io.github.msh91.arch.util.extension.showSnackBar


class CryptoChartFragment : BaseFragment<CryptoChartViewModel, FragmentCryptoChartBinding>() {

    override val viewModel: CryptoChartViewModel by getLazyViewModel(ViewModelScope.FRAGMENT)

    override val layoutId: Int = R.layout.fragment_crypto_chart

    override fun onViewInitialized(binding: FragmentCryptoChartBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.chartInfoLiveData.observe(viewLifecycleOwner, Observer { showDataEntries(it) })
        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer { showSnackBar(it) })
    }

    private fun showDataEntries(chartInfoItem: ChartInfoItem) {
        val cartesian: Cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.padding(10.0, 20.0, 5.0, 20.0)
        cartesian.crosshair().enabled(true)
        cartesian.crosshair().yLabel(false)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.title(chartInfoItem.description)
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)
        cartesian.xAxis(0).title("Last 60 Days")
        val line = cartesian.line(chartInfoItem.entries.map { it.toDataEntry() })
        line.name(chartInfoItem.name)
        line.hovered().markers().enabled(true)
        line.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        line.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)
        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)
        binding.lineChart.setChart(cartesian)
    }
}

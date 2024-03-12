package io.github.msh91.arch.ui.home.chart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import io.github.msh91.arch.R
import io.github.msh91.arch.data.model.Error
import io.github.msh91.arch.data.model.crypto.CryptoChartInfo
import io.github.msh91.arch.data.repository.crypto.CryptoChartRepository
import io.github.msh91.arch.ui.base.BaseViewModel
import io.github.msh91.arch.util.livedata.SingleEventLiveData
import io.github.msh91.arch.util.provider.BaseResourceProvider
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class CryptoChartViewModel @Inject constructor(
    private val cryptoChartRepository: CryptoChartRepository,
    private val resourceProvider: BaseResourceProvider
) : BaseViewModel() {

    val chartInfoLiveData = MutableLiveData<ChartInfoItem>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val errorLiveData = SingleEventLiveData<String>()
    var callInitialized = false

    override fun onStart() {
        super.onStart()
        if (!callInitialized) {
            getBitcoinChart()
            callInitialized = true
        }
    }

    private fun getBitcoinChart() {
        viewModelScope.launch {
            loadingLiveData.value = true
            when (val either = cryptoChartRepository.getChartInfo()) {
                is Either.Left -> showError(either.value)
                is Either.Right -> showChartInfo(either.value)
            }
            loadingLiveData.value = false
        }
    }

    private fun showChartInfo(chartInfo: CryptoChartInfo) {
        val entries = chartInfo.chartValues.map { chartValue ->
            val calendar = Calendar.getInstance()
            calendar.time = chartValue.date
            val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val x = resourceProvider.getString(R.string.holder_day_month, monthName, dayOfMonth)
            ChartEntry(x, chartValue.price)
        }
        chartInfoLiveData.value = ChartInfoItem(chartInfo.name, chartInfo.description, entries)
    }

    private fun showError(error: Error) {
        errorLiveData.value = resourceProvider.getErrorMessage(error)
    }
}

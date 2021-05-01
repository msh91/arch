package io.github.msh91.arch.ui.home.chart

import androidx.lifecycle.MutableLiveData
import io.github.msh91.arch.ui.base.BaseViewModel
import javax.inject.Inject

class CryptoChartViewModel @Inject constructor() : BaseViewModel() {
    val loadingLiveData = MutableLiveData<Boolean>()
}

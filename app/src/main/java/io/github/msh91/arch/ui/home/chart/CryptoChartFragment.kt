package io.github.msh91.arch.ui.home.chart

import io.github.msh91.arch.R
import io.github.msh91.arch.databinding.FragmentCryptoChartBinding
import io.github.msh91.arch.ui.base.BaseFragment
import io.github.msh91.arch.ui.base.ViewModelScope

class CryptoChartFragment : BaseFragment<CryptoChartViewModel, FragmentCryptoChartBinding>() {

    override val viewModel: CryptoChartViewModel by getLazyViewModel(ViewModelScope.FRAGMENT)

    override val layoutId: Int = R.layout.fragment_crypto_chart

    override fun onViewInitialized(binding: FragmentCryptoChartBinding) {
        super.onViewInitialized(binding)

    }
}

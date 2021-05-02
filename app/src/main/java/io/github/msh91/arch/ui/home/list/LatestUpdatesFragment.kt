package io.github.msh91.arch.ui.home.list

import androidx.lifecycle.Observer
import io.github.msh91.arch.R
import io.github.msh91.arch.databinding.FragmentLatestUpdatesBinding
import io.github.msh91.arch.databinding.ItemCryptoCurrencyBinding
import io.github.msh91.arch.ui.base.BaseFragment
import io.github.msh91.arch.ui.base.ViewModelScope
import io.github.msh91.arch.ui.base.adapter.SingleLayoutAdapter
import io.github.msh91.arch.util.extension.observeSafe
import io.github.msh91.arch.util.extension.showSnackBar

class LatestUpdatesFragment : BaseFragment<LatestUpdatesViewModel, FragmentLatestUpdatesBinding>() {

    override val viewModel: LatestUpdatesViewModel by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId: Int = R.layout.fragment_latest_updates

    override fun onViewInitialized(binding: FragmentLatestUpdatesBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        binding.adapter = SingleLayoutAdapter<CryptoCurrencyItem, ItemCryptoCurrencyBinding>(
            layoutId = R.layout.item_crypto_currency,
            onItemClicked = viewModel::onItemClicked
        )
        viewModel.cryptoCurrencyItemsLiveData.observeSafe(viewLifecycleOwner) { binding.adapter?.swapItems(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer { showSnackBar(it) })
    }
}

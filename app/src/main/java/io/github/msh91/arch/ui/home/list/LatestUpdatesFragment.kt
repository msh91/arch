package io.github.msh91.arch.ui.home.list

import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import io.github.msh91.arch.R
import io.github.msh91.arch.databinding.FragmentHomeListBinding
import io.github.msh91.arch.databinding.ItemCryptoCurrencyBinding
import io.github.msh91.arch.ui.base.BaseFragment
import io.github.msh91.arch.ui.base.ViewModelScope
import io.github.msh91.arch.ui.base.adapter.SingleLayoutAdapter
import io.github.msh91.arch.util.extension.observeSafe

class LatestUpdatesFragment : BaseFragment<LatestUpdatesViewModel, FragmentHomeListBinding>() {

    override val viewModel: LatestUpdatesViewModel by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId: Int = R.layout.fragment_home_list

    override fun onViewInitialized(binding: FragmentHomeListBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        binding.adapter = SingleLayoutAdapter<CryptoCurrencyItem, ItemCryptoCurrencyBinding>(
            layoutId = R.layout.item_crypto_currency,
            onItemClicked = viewModel::onItemClicked
        )
        viewModel.cryptoCurrencies.observeSafe(viewLifecycleOwner) { binding.adapter?.swapItems(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer { showSnackBar(it) })
    }

    private fun showSnackBar(message: String) {
        Snackbar
            .make(binding.root, message, Snackbar.LENGTH_LONG)
            .show()
    }
}

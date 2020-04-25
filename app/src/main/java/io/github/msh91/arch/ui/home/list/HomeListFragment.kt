package io.github.msh91.arch.ui.home.list

import android.graphics.Movie
import io.github.msh91.arch.R
import io.github.msh91.arch.databinding.FragmentHomeListBinding
import io.github.msh91.arch.databinding.ItemMovieBinding
import io.github.msh91.arch.ui.base.BaseFragment
import io.github.msh91.arch.ui.base.ViewModelScope
import io.github.msh91.arch.ui.base.adapter.SingleLayoutAdapter
import io.github.msh91.arch.util.extension.observe
import io.github.msh91.arch.util.extension.observeSafe

class HomeListFragment : BaseFragment<HomeListViewModel, FragmentHomeListBinding>() {
    companion object {
        fun newInstance() = HomeListFragment().apply {
            //            setArguments(Pair("key", value))
        }
    }

    override val viewModel: HomeListViewModel by getLazyViewModel(ViewModelScope.ACTIVITY)
    override val layoutId: Int = R.layout.fragment_home_list

    override fun onViewInitialized(binding: FragmentHomeListBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        binding.adapter = SingleLayoutAdapter<Movie, ItemMovieBinding>(
                R.layout.item_movie,
                emptyList(),
                viewModel
        )

        viewModel.movies.observeSafe(viewLifecycleOwner) { binding.adapter?.swapItems(it) }
    }
}
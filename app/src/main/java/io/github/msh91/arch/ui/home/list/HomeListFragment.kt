package io.github.msh91.arch.ui.home.list

import io.github.msh91.arch.R
import io.github.msh91.arch.databinding.FragmentHomeListBinding
import io.github.msh91.arch.ui.base.BaseFragment
import io.github.msh91.arch.ui.base.ViewModelScope

class HomeListFragment : BaseFragment<HomeListViewModel, FragmentHomeListBinding>() {
    companion object {
        fun newInstance() = HomeListFragment().apply {
            //            setArguments(Pair("key", value))
        }
    }

    override val viewModel: HomeListViewModel by getLazyViewModel(ViewModelScope.ACTIVITY)
    override val layoutId: Int = R.layout.fragment_home_list

}
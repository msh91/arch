package io.github.msh91.arch.ui.base.adapter

import androidx.databinding.ViewDataBinding
import io.github.msh91.arch.ui.base.BaseViewModel

/**
 * Simplest implementation of [BaseAdapter] to use as a single layout adapter.
 */
open class SingleLayoutAdapter<T, B : ViewDataBinding>(
    private val layoutId: Int,
    items: List<T>,
    viewModel: BaseViewModel? = null,
    onBind: B.(Int) -> Unit = {}
) : BaseAdapter<T, B>(viewModel = viewModel, items = items, onBind = onBind) {

    override fun getLayoutId(position: Int): Int = layoutId
}

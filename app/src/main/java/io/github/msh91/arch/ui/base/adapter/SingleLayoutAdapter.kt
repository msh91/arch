package io.github.msh91.arch.ui.base.adapter

import androidx.databinding.ViewDataBinding

/**
 * Simple implementation of [BaseAdapter] to use as a single layout adapter.
 */
open class SingleLayoutAdapter<T : Any, B : ViewDataBinding>(
    private val layoutId: Int,
    items: List<T> = emptyList(),
    onItemClicked: ((T) -> Unit)? = null,
    onBind: B.(Int) -> Unit = {}
) : BaseAdapter<T, B>(items = items, onItemClicked = onItemClicked, onBind = onBind) {

    override fun getLayoutId(position: Int): Int = layoutId
}

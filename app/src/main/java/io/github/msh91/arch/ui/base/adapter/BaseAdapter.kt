package io.github.msh91.arch.ui.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.msh91.arch.BR

/**
 * An abstract Adapter that extends [RecyclerView.Adapter] and can be used as base adapter in layouts provided by
 * DataBinding.
 *
 * @param itemBindingId Generated item binding id that will should be founded in BR class and will be
 * used in [BaseViewHolder.bind] method.
 *
 * @param items list of items to be shown. Can be changed later by calling [swapItems].
 * @param onItemClicked click listener to be invoked when user clicks on item
 * @param onBind an extension function on [B] that receives position of current item and allows us
 * to access binding class outside of [BaseAdapter].
 *
 */
abstract class BaseAdapter<T : Any, B : ViewDataBinding>(
    private val itemBindingId: Int = BR.item,
    items: List<T> = emptyList(),
    private val onItemClicked: ((T) -> Unit)? = null,
    private val onBind: B.(Int) -> Unit = {}
) : RecyclerView.Adapter<BaseViewHolder<T, B>>() {
    private val items = mutableListOf<T>().apply {
        addAll(items)
    }

    /**
     * get item at given position
     */
    fun getItem(position: Int): T = items[position]

    override fun getItemCount(): Int = items.size

    /**
     * abstract function to decide which layout should be shown at given position.
     * This will be useful for multi layout adapters. for single layout adapter it can only returns
     * a static layout resource id.
     *
     * @return relevant layout resource id based on given position
     *
     */
    abstract fun getLayoutId(position: Int): Int

    /**
     * Instead of returning viewType, this method will return layout id at given position provided
     * by [getLayoutId] and will be used in [onCreateViewHolder].
     *
     * @see [RecyclerView.Adapter.getItemViewType]
     */
    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position)
    }

    /**
     * Attempt to create an instance of [BaseViewHolder] with inflated Binding class
     *
     * @param viewType will be used as layoutId for [DataBindingUtil] and will be provided by [getItemViewType]
     *
     * @see [RecyclerView.Adapter.onCreateViewHolder]
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T, B> {
        val inflater = LayoutInflater.from(parent.context)
        val binding: B = DataBindingUtil.inflate(inflater, viewType, parent, false)
        return BaseViewHolder(binding)
    }

    /**
     * Attempt to bind item at given position to holder.
     * And also attempts to invoke [onBind] lambda
     * function on instance of [B] in [BaseViewHolder.binding].
     *
     * @see [RecyclerView.Adapter.onBindViewHolder]
     */
    override fun onBindViewHolder(holder: BaseViewHolder<T, B>, position: Int) {
        holder.bind(itemBindingId, getItem(position))
        holder.binding.onBind(position)
    }

    /**
     * Attempts to replace current list of items with newly provided items and notify adapter
     * based on differences of these two lists by [DiffUtil]
     */
    open fun swapItems(newItems: List<T>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition] == newItems[newItemPosition]

            override fun getOldListSize(): Int =
                items.size

            override fun getNewListSize(): Int =
                newItems.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition] == newItems[newItemPosition]
        })
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newItems)
    }
}

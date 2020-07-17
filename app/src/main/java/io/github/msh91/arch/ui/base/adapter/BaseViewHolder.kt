package io.github.msh91.arch.ui.base.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * A wrapper for [RecyclerView.ViewHolder] in order to use in [BaseAdapter]
 * @param T type of data model that will be set in Binding class
 * @param B A [ViewDataBinding] extended class that representing Binding for item layout
 *
 * @param binding an instance of [B] to get root view for [RecyclerView.ViewHolder] constructor and
 * display data model
 * @param onItemClicked click listener to be invoked when user clicks on item
 */
class BaseViewHolder<in T : Any, out B : ViewDataBinding>(
    val binding: B,
    onItemClicked: ((T) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: T

    init {
        itemView.setOnClickListener { onItemClicked?.invoke(item) }
    }

    /**
     * binding method that bind data model to [ViewDataBinding] class
     *
     * @param itemBindingId Generated item binding id that will should be founded in BR class.
     * @param item an instance of [T] to be shown in layout
     */
    fun bind(itemBindingId: Int, item: T) {
        this.item = item
        binding.setVariable(itemBindingId, item)
        binding.executePendingBindings()
    }
}

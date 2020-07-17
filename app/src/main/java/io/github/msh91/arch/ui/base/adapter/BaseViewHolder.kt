package io.github.msh91.arch.ui.base.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import io.github.msh91.arch.ui.base.BaseViewModel

/**
 * A wrapper for [RecyclerView.ViewHolder] in order to use in [BaseAdapter]
 * @param T type of data model that will be set in Binding class
 * @param B A [ViewDataBinding] extended class that representing Binding for item layout
 *
 * @param binding an instance of [B] to get root view for [RecyclerView.ViewHolder] constructor and
 * display data model
 */
class BaseViewHolder<in T, out B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root) {

    /**
     * binding method that bind data model to [ViewDataBinding] class
     *
     * @param itemBindingId Generated item binding id that will should be founded in BR class.
     * @param item an instance of [T] to be shown in layout
     */
    fun bind(itemBindingId: Int, item: T, viewModelBindingId: Int, viewModel: BaseViewModel?) {
        binding.setVariable(itemBindingId, item)
        if (viewModel != null) {
            binding.setVariable(viewModelBindingId, viewModel)
        }
        binding.executePendingBindings()
    }
}

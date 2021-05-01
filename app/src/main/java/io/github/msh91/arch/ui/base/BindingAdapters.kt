package io.github.msh91.arch.ui.base

import android.view.View
import androidx.databinding.BindingAdapter

class BindingAdapters {
    companion object {
        @BindingAdapter("app:changeVisibility")
        @JvmStatic
        fun changeVisibility(view: View, value: Boolean) {
            view.visibility = if (value) View.VISIBLE else View.GONE
        }
    }

}

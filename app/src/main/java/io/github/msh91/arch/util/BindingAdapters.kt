package io.github.msh91.arch.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import io.github.msh91.arch.app.GlideApp

class BindingAdapters {
    companion object {

        @JvmStatic
        @BindingAdapter("requestFocusListener")
        fun requestFocus(editText: EditText, shouldFocus: Boolean) {
            if (shouldFocus) {
                editText.postDelayed({ editText.requestFocus() }, 100)
            } else {
                editText.postDelayed({ editText.clearFocus() }, 100)
            }
        }

        @JvmStatic
        @BindingAdapter("bindValue")
        fun bindEditTextValue(editText: EditText, field: MutableLiveData<String>) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    field.value = s.toString()
                }
            })
        }

        /*@JvmStatic
        @BindingAdapter("changeSearchViewFocusListener")
        fun changeSearchViewFocus(editText: EditText, searchIcon: AppCompatImageView) {
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    searchIcon.setImageResource(R.drawable.vector_search_black_87)
                } else {
                    searchIcon.setImageResource(R.drawable.vector_search_black_54)
                }
            }
        }*/

        @JvmStatic
        @BindingAdapter("srcImageUrl")
        fun setImageSrc(imageView: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                GlideApp.with(imageView.context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("matchSrcImageUrl")
        fun setMatchImageSrc(imageView: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                GlideApp.with(imageView.context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("srcImageBitmap")
        fun setImageBitmap(imageView: ImageView, bitmap: Bitmap?) {
            bitmap?.let {
                GlideApp.with(imageView.context).load(it)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("srcImageDrawable")
        fun setImageDrawable(imageView: ImageView, drawable: Drawable?) {
            drawable?.let {
                GlideApp.with(imageView.context).load(it)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("app:srcCompat")
        fun setSrcCompatResId(imageView: AppCompatImageView, @DrawableRes drawableResId: Int) {
            imageView.setImageResource(drawableResId)
        }

        @JvmStatic
        @BindingAdapter("app:srcCompat")
        fun setSrcCompatDrawable(imageView: AppCompatImageView, drawable: Drawable) {
            imageView.setImageDrawable(drawable)
        }

        @JvmStatic
        @BindingAdapter("android:background")
        fun setBackgroundResId(view: View, @DrawableRes drawableResId: Int) {
            view.setBackgroundResource(drawableResId)
        }
    }
}

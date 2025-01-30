package io.github.msh91.arcyto.core.data.local.resource

import android.content.Context
import com.squareup.anvil.annotations.ContributesBinding
import io.github.msh91.arcyto.core.di.scope.AppScope
import javax.inject.Inject

interface StringProvider {
    fun getString(id: Int): String
    fun getString(id: Int, vararg args: Any): String
}

@ContributesBinding(AppScope::class)
class StringProviderImpl @Inject constructor(private val context: Context) : StringProvider {

    override fun getString(id: Int): String = context.getString(id)

    override fun getString(id: Int, vararg args: Any): String = context.getString(id, *args)
}
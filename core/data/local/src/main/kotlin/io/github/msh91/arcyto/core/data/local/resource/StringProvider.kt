package io.github.msh91.arcyto.core.data.local.resource

import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

interface StringProvider {
    fun getString(id: Int): String

    fun getString(
        id: Int,
        vararg args: Any,
    ): String
}

@Inject
@ContributesBinding(AppScope::class)
class StringProviderImpl(
    private val context: Context,
) : StringProvider {
    override fun getString(id: Int): String = context.getString(id)

    override fun getString(
        id: Int,
        vararg args: Any,
    ): String = context.getString(id, *args)
}

package io.github.msh91.arcyto.details.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class DetailsRouteRequest(
    val coinId: String,
    val date: Long,
) : NavKey

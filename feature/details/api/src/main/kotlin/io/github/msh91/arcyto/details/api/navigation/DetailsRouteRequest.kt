package io.github.msh91.arcyto.details.api.navigation

import kotlinx.serialization.Serializable

@Serializable
data class DetailsRouteRequest(val coinId: String, val date: Long)
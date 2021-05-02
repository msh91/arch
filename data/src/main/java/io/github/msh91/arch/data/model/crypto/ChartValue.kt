package io.github.msh91.arch.data.model.crypto

import com.google.gson.annotations.SerializedName
import java.util.*

data class ChartValue(
    @SerializedName("x") val date: Date,
    @SerializedName("y") val price: Float
)

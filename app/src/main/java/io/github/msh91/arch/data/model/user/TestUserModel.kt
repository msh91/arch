package io.github.msh91.arch.data.model.user

import com.google.gson.annotations.SerializedName

data class TestUserModel(
        @SerializedName("name") val name: String,
        @SerializedName("family") val family: String
)
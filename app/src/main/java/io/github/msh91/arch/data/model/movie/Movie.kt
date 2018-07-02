package io.github.msh91.arch.data.model.movie

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Movie(
        @PrimaryKey val id: Int,
        val name: String
)
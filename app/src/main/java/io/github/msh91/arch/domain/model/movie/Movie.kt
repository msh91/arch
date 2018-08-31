package io.github.msh91.arch.domain.model.movie

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Movie(
        @PrimaryKey val id: String,
        val name: String,
        val avatar: String
)
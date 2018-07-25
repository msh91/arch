package io.github.msh91.arch.data.source.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import io.github.msh91.arch.domain.model.movie.Movie
import io.github.msh91.arch.data.source.db.dao.MovieDao

@Database(entities = [Movie::class], version = AppDataBase.VERSION)
abstract class AppDataBase : RoomDatabase() {

    companion object {
        const val DB_NAME = "arch.db"
        const val VERSION = 1
    }

    abstract fun movieDao(): MovieDao
}
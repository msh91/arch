package io.github.msh91.arch.data.source.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.msh91.arch.data.source.db.dao.MovieDao
import io.github.msh91.arch.domain.model.movie.Movie

@Database(entities = [Movie::class], version = AppDataBase.VERSION)
abstract class AppDataBase : RoomDatabase() {

    companion object {
        const val DB_NAME = "arch.db"
        const val VERSION = 2
    }

    abstract fun movieDao(): MovieDao
}
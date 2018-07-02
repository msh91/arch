package io.github.msh91.arch.data.source.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.github.msh91.arch.data.model.movie.Movie
import io.reactivex.Flowable

@Dao
interface MovieDao {
    @Insert
    fun insertMovie(movie: Movie): Long

    @Insert
    fun insertMovies(movies: List<Movie>): List<Long>

    @Query("SELECT COUNT(*) FROM Movie")
    fun getCount(): Int

    @Query("SELECT * FROM Movie")
    fun getAllMovies(): Flowable<List<Movie>>

    @Query("SELECT * FROM Movie WHERE id = :id")
    fun getMovieById(id: Int): Flowable<Movie>

    @Query("SELECT * FROM Movie WHERE name LIKE '%'||:name||'%'")
    fun getMoviesByName(name: String): Flowable<List<Movie>>
}
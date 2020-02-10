package io.github.msh91.arch.data.source.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.msh91.arch.data.model.movie.Movie

@Dao
interface MovieDao {
    @Insert
    suspend fun insertMovie(movie: Movie): Long

    @Insert
    suspend fun insertMovies(movies: List<Movie>): List<Long>

    @Query("SELECT COUNT(*) FROM Movie")
    suspend fun getCount(): Int

    @Query("SELECT * FROM Movie")
    suspend fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM Movie WHERE id = :id")
    suspend fun getMovieById(id: Int): Movie

    @Query("SELECT * FROM Movie WHERE name LIKE '%'||:name||'%'")
    suspend fun getMoviesByName(name: String): List<Movie>
}
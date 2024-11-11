package com.example.dataemail.database

import androidx.room.*
import com.example.dataemail.database.ParcelableMovie
import com.example.dataemail.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): Movie?

    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Int)

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()
}


fun Movie.toParcelable(): ParcelableMovie {
    return ParcelableMovie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        releaseDate = this.releaseDate,
        posterPath = this.posterPath,
        voteAverage = this.voteAverage,
        genre = this.genre,
        duration = this.duration,
        ageRestriction = this.ageRestriction
    )
}

fun ParcelableMovie.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        releaseDate = this.releaseDate,
        posterPath = this.posterPath,
        voteAverage = this.voteAverage,
        genre = this.genre,
        duration = this.duration,
        ageRestriction = this.ageRestriction
    )
}

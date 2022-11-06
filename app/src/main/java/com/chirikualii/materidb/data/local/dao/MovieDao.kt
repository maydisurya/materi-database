package com.chirikualii.materidb.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chirikualii.materidb.data.local.entity.MovieEntity
import com.chirikualii.materidb.data.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("Select * FROM movie WHERE type_movie = (:typeMovie)")
    suspend fun getListMoviesNowPlaying(
        typeMovie: String
    ): List<MovieEntity>

    @Query("Select * FROM movie WHERE type_movie = (:typeMovie)")
    suspend fun getListMoviePopular(
        typeMovie: String
    ): List<MovieEntity>

    @Update(entity = MovieEntity::class)
    suspend fun  updateMovie(movieEntity: MovieEntity)

    @Query("Update MOVIE set bookmark = :bookmark where movieId = :movieId")
    suspend fun  updateMovieWithQuery(movieId: String, bookmark:Int)
}
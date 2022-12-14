package com.chirikualii.materidb.data.repository

import com.chirikualii.materidb.data.model.Movie

interface MovieRepo {
    suspend fun getPopularMovie(): List<Movie>
    suspend fun getNowPlayingMovie(): List<Movie>
    suspend fun getPopuarMovieLocal(): List<Movie>
    suspend fun getNowplayingLocal(): List<Movie>
}
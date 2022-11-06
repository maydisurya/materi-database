package com.chirikualii.materidb.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.chirikualii.materidb.data.local.MovieDbImpl
import com.chirikualii.materidb.data.local.entity.MovieEntity
import com.chirikualii.materidb.data.local.entity.MovieType
import com.chirikualii.materidb.data.model.Movie
import com.chirikualii.materidb.data.remote.ApiService
import com.google.gson.Gson
import kotlin.math.log

class MovieRepoImpl(
    private val service: ApiService,
    private val movieDb: MovieDbImpl
    ): MovieRepo {

    override suspend fun getPopularMovie(): List<Movie> {
       try {
           val response = service.getPopularMovie()

           if(response.isSuccessful){
               val listMovie = response.body()

               val lisMovieEntity = listMovie?.results?.map {
                   MovieEntity(
                       title = it.title,
                       movieId = it.id.toString(),
                       releaseDate = it.releaseDate,
                       imagePoster = it.posterPath,
                       overview = it.overview,
                       backdrop = it.backdropPath,
                       typeMovie = MovieType.popular
                   )
               }

               lisMovieEntity?.forEach {
                   movieDb.getDatabase().movieDao().insertMovie(it)
               }
               return getPopuarMovieLocal() ?: emptyList()
           }else{
               Log.e(MovieRepoImpl::class.simpleName,
                   "getPopularMovie error code: ${response.code()}", )
               return getPopuarMovieLocal()
           }
       }catch (e:Exception){
           Log.e(MovieRepoImpl::class.simpleName, "getPopularMovie error :${e.message} ", )
           return getPopuarMovieLocal()
       }
    }

    override suspend fun getNowPlayingMovie(): List<Movie> {
        try {
            val response = service.getNowPlayingMovie()

            if(response.isSuccessful){
                val listMovie = response.body()

                val lisMovieEntity = listMovie?.results?.map {
                    MovieEntity(
                        title = it.title,
                        movieId = it.id.toString(),
                        releaseDate = it.releaseDate,
                        imagePoster = it.posterPath,
                        overview = it.overview,
                        backdrop = it.backdropPath,
                        typeMovie = MovieType.nowPlaying
                    )
                }
                Log.d(MovieRepoImpl::class.simpleName,
                    "getNowPlayingMovie : ${Gson().toJsonTree(lisMovieEntity)}")

                lisMovieEntity?.forEach {
                    movieDb.getDatabase().movieDao().insertMovie(it)
                }
                return getNowplayingLocal() ?: emptyList()
            }else{
                Log.e(MovieRepoImpl::class.simpleName,
                    "getNowPlayingMovie error code: ${response.code()}", )
                return getNowplayingLocal()
            }
        }catch (e:Exception){
            Log.e(MovieRepoImpl::class.simpleName, "getNowPlayingMovie error :${e.message} ", )
            return getNowplayingLocal()
        }
    }

    override suspend fun getPopuarMovieLocal(): List<Movie> {
        var listddata = listOf<Movie>()
        try {
            listddata = movieDb.getDatabase().movieDao().getListMoviePopular(
                MovieType.popular
            ).map {
                Movie(
                    movieId = it.movieId,
                    title = it.title,
                    releaseDate = it.releaseDate,
                    imagePoster = it.imagePoster,
                    backdrop =  it.backdrop,
                    overview = it.overview,
                    bookmark = it.bookmark
                )
            }

            return listddata
        }catch (e:Exception){
            Log.e(MovieRepoImpl::class.simpleName, "getPopuarMovieLocal: error ${e.message}")
            return listddata
        }
    }

    override suspend fun getNowplayingLocal(): List<Movie> {
        var listData = listOf<Movie>()

        try {
            listData = movieDb.getDatabase().movieDao().getListMoviesNowPlaying(
                MovieType.nowPlaying
            ).map {
                Movie(
                    movieId = it.movieId,
                    title = it.title,
                    releaseDate = it.releaseDate,
                    imagePoster = it.imagePoster,
                    backdrop = it.backdrop,
                    overview = it.overview,
                    bookmark = it.bookmark,
                )
            }

            return listData
        }catch (e:Exception){
            Log.e(MovieRepoImpl::class.simpleName, "getNowplayingLocal: eror ${e.message}", )
            return listData
        }
    }
}
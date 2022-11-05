package com.chirikualii.materidb.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.chirikualii.materidb.data.local.MovieDbImpl
import com.chirikualii.materidb.data.local.entity.MovieEntity
import com.chirikualii.materidb.data.local.entity.MovieType
import com.chirikualii.materidb.data.model.Movie
import com.chirikualii.materidb.data.remote.ApiService
import com.google.gson.Gson

class MovieRepoImpl(
    private val service: ApiService,
    private val movieDb: MovieDbImpl
    ): MovieRepo {

    override suspend fun getPopularMovie(): List<Movie> {
       try {
           val response = service.getPopularMovie()

           if(response.isSuccessful){
               val listMovie = response.body()
               val listData = listMovie?.results?.map {
                   Movie(
                       title = it.title,
                       releaseDate = it.releaseDate,
                       imagePoster = it.posterPath,
                       overview = it.overview,
                       backdrop = it.backdropPath
                   )
               }
               Log.d(MovieRepoImpl::class.simpleName,
                   "getPopularMovie : ${Gson().toJsonTree(listData)}")

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
               return listData ?: emptyList()
           }else{
               Log.e(MovieRepoImpl::class.simpleName,
                   "getPopularMovie error code: ${response.code()}", )
               return emptyList()
           }
       }catch (e:Exception){
           Log.e(MovieRepoImpl::class.simpleName, "getPopularMovie error :${e.message} ", )
           return emptyList()
       }
    }

    override suspend fun getNowPlayingMovie(): List<Movie> {
        try {
            val response = service.getNowPlayingMovie()

            if(response.isSuccessful){
                val listMovie = response.body()
                val listData = listMovie?.results?.map {
                    Movie(
                        title = it.title,
                        releaseDate = it.releaseDate,
                        imagePoster = it.posterPath,
                        overview = it.overview,
                        backdrop = it.backdropPath
                    )
                }
                Log.d(MovieRepoImpl::class.simpleName,
                    "getNowPlayingMovie : ${Gson().toJsonTree(listData)}")


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

                lisMovieEntity?.forEach {
                    movieDb.getDatabase().movieDao().insertMovie(it)
                }
                return listData ?: emptyList()
            }else{
                Log.e(MovieRepoImpl::class.simpleName,
                    "getNowPlayingMovie error code: ${response.code()}", )
                return emptyList()
            }
        }catch (e:Exception){
            Log.e(MovieRepoImpl::class.simpleName, "getNowPlayingMovie error :${e.message} ", )
            return emptyList()
        }
    }
}
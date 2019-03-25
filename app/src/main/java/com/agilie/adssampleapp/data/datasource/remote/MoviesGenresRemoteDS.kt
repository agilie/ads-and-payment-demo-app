package com.agilie.adssampleapp.data.datasource.remote

import com.agilie.adssampleapp.BuildConfig
import com.agilie.adssampleapp.domain.model.MovieGenre
import com.agilie.adssampleapp.network.MovieDbApi
import com.agilie.adssampleapp.network.RetrofitServiceGenerator
import io.reactivex.Single

class MoviesGenresRemoteDS: IMoviesGenresRemoteDataSource {

    private val apiService: MovieDbApi by lazy {
        return@lazy RetrofitServiceGenerator.createService(MovieDbApi::class.java)
    }

    override fun getMoviesGenres(): Single<List<MovieGenre>> {
        return apiService.getMoviesGenres(BuildConfig.THEMOVIEDB_API_KEY)
            .map { it.genres }
    }
}
package com.agilie.adssampleapp.data.datasource.remote

import com.agilie.adssampleapp.BuildConfig
import com.agilie.adssampleapp.domain.model.MovieDetails
import com.agilie.adssampleapp.network.MovieDbApi
import com.agilie.adssampleapp.network.RetrofitServiceGenerator
import io.reactivex.Single

class MovieDetailsRemoteDS: IMovieDetailsRemoteDataSource {

    private val apiService: MovieDbApi by lazy {
        return@lazy RetrofitServiceGenerator.createService(MovieDbApi::class.java)
    }

    override fun getMovieDetails(movieId: Long, language: String?): Single<MovieDetails> {
        return apiService.getMovieDetails(movieId, BuildConfig.THEMOVIEDB_API_KEY, language)
    }
}
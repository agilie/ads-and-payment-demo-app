package com.agilie.adssampleapp.data.datasource.remote

import com.agilie.adssampleapp.BuildConfig
import com.agilie.adssampleapp.domain.model.MovieCharacter
import com.agilie.adssampleapp.network.MovieDbApi
import com.agilie.adssampleapp.network.RetrofitServiceGenerator
import io.reactivex.Single

class MovieCharactersRemoteDS : IMovieCharactersRemoteDataSource {

    private val apiService: MovieDbApi by lazy {
        return@lazy RetrofitServiceGenerator.createService(MovieDbApi::class.java)
    }

    override fun getMovieDetails(movieId: Long): Single<List<MovieCharacter>> {
        return apiService.getMovieCredits(movieId, BuildConfig.THEMOVIEDB_API_KEY)
            .map { it.cast }
            .flattenAsObservable { cast -> cast }
            .doOnNext { character -> character.movieId = movieId }
            .toList()
    }
}
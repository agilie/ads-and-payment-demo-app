package com.agilie.adssampleapp.data.repository

import com.agilie.adssampleapp.BuildConfig
import com.agilie.adssampleapp.data.datasource.local.IMovieDetailsLocalDataSource
import com.agilie.adssampleapp.data.datasource.remote.IMovieDetailsRemoteDataSource
import com.agilie.adssampleapp.domain.model.MovieCharacter
import com.agilie.adssampleapp.domain.model.MovieDetails
import com.agilie.adssampleapp.network.MovieDbApi
import com.agilie.adssampleapp.network.RetrofitServiceGenerator
import io.reactivex.Single
import io.reactivex.functions.Function
import java.util.*

class MoviesDetailsRepository(
    private val localDS: IMovieDetailsLocalDataSource,
    private val remoteDS: IMovieDetailsRemoteDataSource
) {

    private val apiService: MovieDbApi by lazy {
        return@lazy RetrofitServiceGenerator.createService(MovieDbApi::class.java)
    }

    fun getMovieDetails(movieId: Long): Single<MovieDetails> {
        return localDS.getMovieDetails(movieId)
            .onErrorResumeNext(Function { error ->
                if (error is NullPointerException) {
                    return@Function remoteDS
                        .getMovieDetails(
                            movieId,
                            Locale.getDefault().displayLanguage
                        )
                        .flatMap { loaded ->
                            localDS.saveMovieDetails(loaded)
                                .andThen(Single.just(loaded))
                        }
                } else {
                    return@Function Single.error(error)
                }
            })
    }

    fun getMoviesCast(movieId: Long): Single<List<MovieCharacter>> {
        return apiService.getMovieCredits(movieId, BuildConfig.THEMOVIEDB_API_KEY)
            .map { it.cast }
            .flattenAsObservable { cast -> cast }
            .doOnNext { character -> character.movieId = movieId }
            .toList()
    }
}
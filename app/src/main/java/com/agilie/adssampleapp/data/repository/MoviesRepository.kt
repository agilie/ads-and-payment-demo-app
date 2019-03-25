package com.agilie.adssampleapp.data.repository

import com.agilie.adssampleapp.BuildConfig
import com.agilie.adssampleapp.data.datasource.local.IMoviesLocalDataSource
import com.agilie.adssampleapp.domain.model.Movie
import com.agilie.adssampleapp.network.MovieDbApi
import com.agilie.adssampleapp.network.RetrofitServiceGenerator
import io.reactivex.Single
import java.util.*

class MoviesRepository(private val localDS: IMoviesLocalDataSource) {

    private val apiService: MovieDbApi by lazy {
        return@lazy RetrofitServiceGenerator.createService(MovieDbApi::class.java)
    }

    fun getMovies(): Single<List<Movie>> {
        return localDS.getMovies()
            .flatMap { movies ->
                if (movies.isEmpty()) {
                    return@flatMap apiService
                        .getUpcomingMovies(BuildConfig.THEMOVIEDB_API_KEY, Locale.getDefault().displayLanguage, 1)
                        .map { it.movies }
                        .flatMap { loaded ->
                            localDS.saveMovies(loaded)
                                .andThen(Single.just(loaded))
                        }
                } else {
                    return@flatMap Single.just(movies)
                }
            }

    }
}
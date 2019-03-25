package com.agilie.adssampleapp.data.repository

import com.agilie.adssampleapp.data.datasource.local.IMoviesGenresLocalDataSource
import com.agilie.adssampleapp.data.datasource.remote.IMoviesGenresRemoteDataSource
import com.agilie.adssampleapp.domain.model.MovieGenre
import io.reactivex.Single

class MoviesGenresRepository(
    private val localDS: IMoviesGenresLocalDataSource,
    private val remoteDS: IMoviesGenresRemoteDataSource
) {

    fun getMovies(): Single<List<MovieGenre>> {
        return localDS.getMoviesGenres()
            .flatMap { genres ->
                if (genres.isEmpty()) {
                    return@flatMap remoteDS
                        .getMoviesGenres()
                        .flatMap { loaded ->
                            localDS.saveMoviesGenres(loaded)
                                .andThen(Single.just(loaded))
                        }
                } else {
                    return@flatMap Single.just(genres)
                }
            }

    }
}
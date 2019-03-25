package com.agilie.adssampleapp.data.datasource.local

import com.agilie.adssampleapp.domain.model.MovieDetails
import io.reactivex.Completable
import io.reactivex.Single

interface IMovieDetailsLocalDataSource {
    fun getMovieDetails(movieId: Long): Single<MovieDetails>

    fun saveMovieDetails(details: MovieDetails): Completable
}
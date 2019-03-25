package com.agilie.adssampleapp.data.datasource.local

import com.agilie.adssampleapp.domain.model.Movie
import io.reactivex.Completable
import io.reactivex.Single

interface IMoviesLocalDataSource {
    fun getMovies() : Single<List<Movie>>

    fun saveMovies(movies: List<Movie>): Completable
}
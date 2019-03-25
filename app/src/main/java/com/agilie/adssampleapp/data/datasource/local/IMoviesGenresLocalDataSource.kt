package com.agilie.adssampleapp.data.datasource.local

import com.agilie.adssampleapp.domain.model.MovieGenre
import io.reactivex.Completable
import io.reactivex.Single

interface IMoviesGenresLocalDataSource {
    fun getMoviesGenres(): Single<List<MovieGenre>>

    fun saveMoviesGenres(genres: List<MovieGenre>): Completable
}
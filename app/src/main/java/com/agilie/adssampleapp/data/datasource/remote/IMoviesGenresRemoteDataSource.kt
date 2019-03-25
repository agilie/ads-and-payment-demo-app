package com.agilie.adssampleapp.data.datasource.remote

import com.agilie.adssampleapp.domain.model.MovieGenre
import io.reactivex.Single

interface IMoviesGenresRemoteDataSource {
    fun getMoviesGenres(): Single<List<MovieGenre>>
}
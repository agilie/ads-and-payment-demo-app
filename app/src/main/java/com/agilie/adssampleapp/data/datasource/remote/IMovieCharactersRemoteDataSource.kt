package com.agilie.adssampleapp.data.datasource.remote

import com.agilie.adssampleapp.domain.model.MovieCharacter
import io.reactivex.Single

interface IMovieCharactersRemoteDataSource {
    fun getMovieDetails(movieId: Long): Single<List<MovieCharacter>>
}
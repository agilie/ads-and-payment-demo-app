package com.agilie.adssampleapp.data.datasource.remote

import com.agilie.adssampleapp.domain.model.MovieDetails
import io.reactivex.Single

interface IMovieDetailsRemoteDataSource {
    fun getMovieDetails(movieId: Long, language: String?): Single<MovieDetails>
}
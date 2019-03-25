package com.agilie.adssampleapp.data.datasource.remote

import com.agilie.adssampleapp.domain.model.MovieSession
import io.reactivex.Single

interface IMoviesScheduleRemoteDataSource {
    fun fetchMovieSchedule(cinemaId: Int, movieId: Long): Single<List<MovieSession>>
}
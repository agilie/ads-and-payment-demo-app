package com.agilie.adssampleapp.data.datasource.local

import com.agilie.adssampleapp.domain.model.MovieSession
import io.reactivex.Completable
import io.reactivex.Single

interface IMoviesScheduleLocalDataSource {
    fun getMovieSchedule(cinemaId: Int, movieId: Long): Single<List<MovieSession>>

    fun saveMovieSchedule(sessions: List<MovieSession>): Completable
}
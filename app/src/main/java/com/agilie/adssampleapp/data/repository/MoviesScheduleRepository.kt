package com.agilie.adssampleapp.data.repository

import com.agilie.adssampleapp.data.datasource.local.IMoviesScheduleLocalDataSource
import com.agilie.adssampleapp.data.datasource.remote.IMoviesScheduleRemoteDataSource
import com.agilie.adssampleapp.domain.model.MovieSession
import io.reactivex.Single

class MoviesScheduleRepository(
    private val localDS: IMoviesScheduleLocalDataSource,
    private val remoteDS: IMoviesScheduleRemoteDataSource
) {

    fun getMovieSchedule(cinemaId: Int, movieId: Long): Single<List<MovieSession>> {
        return localDS.getMovieSchedule(cinemaId, movieId)
            .flatMap {
                return@flatMap if (it.isNullOrEmpty()) {
                    remoteDS.fetchMovieSchedule(cinemaId, movieId)
                        .flatMap { schedules ->
                            localDS.saveMovieSchedule(schedules).toSingle { schedules }
                        }
                } else {
                    Single.just(it)
                }
            }
    }
}
package com.agilie.adssampleapp.data.datasource.remote

import com.agilie.adssampleapp.data.ResponseToMovieSession
import com.agilie.adssampleapp.data.model.MovieScheduleResponse
import com.agilie.adssampleapp.domain.model.MovieSession
import com.agilie.adssampleapp.domain.model.MovieSessionAvailable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

class MoviesScheduleRemoteDS : IMoviesScheduleRemoteDataSource {

    override fun fetchMovieSchedule(cinemaId: Int, movieId: Long): Single<List<MovieSession>> {
        val mapper = ResponseToMovieSession().getMapper()
        val now = Calendar.getInstance()
        return generateMoviesScheduler(cinemaId, movieId).flattenAsObservable { it }
            .map(mapper)
            .filter { it.date.after(now.time) }
            .toList()
    }

    //TODO add implementation to generate movies schedules
    private fun generateMoviesScheduler(cinemaId: Int, movieId: Long): Single<List<MovieScheduleResponse>> {
        val randomDays = Math.round((Math.random() * 3)).toInt() + 6
        val date = (0..randomDays).toList()
        val time = listOf(9 to 30, 12 to 15, 13 to 30, 16 to 0, 19 to 45, 21 to 0, 23 to 0)
        return Observable.fromIterable(date)
            .map {
                return@map Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, it) }
            }
            .flatMap { calendar ->
                Observable.fromIterable(time)
                    .map {
                        val (hours, minutes) = it
                        return@map Calendar.getInstance().apply {
                            set(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH),
                                hours,
                                minutes,
                                0
                            )
                        }
                    }
            }
            .map {
                val available = generateAvailable()
                MovieScheduleResponse(cinemaId, movieId, it.time, available)
            }
            .toList()
    }

    private fun generateAvailable(): MovieSessionAvailable {
        val random = Math.round((Math.random() * 6)).toInt()
        return when (random) {
            0 -> MovieSessionAvailable.SOLD_OUT
            1 -> MovieSessionAvailable.FEW_LEFT
            else -> MovieSessionAvailable.AVAILABLE
        }
    }
}
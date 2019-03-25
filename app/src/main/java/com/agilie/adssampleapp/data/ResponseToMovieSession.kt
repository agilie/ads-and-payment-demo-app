package com.agilie.adssampleapp.data

import com.agilie.adssampleapp.data.model.MovieScheduleResponse
import com.agilie.adssampleapp.domain.model.MovieSession
import io.reactivex.functions.Function

class ResponseToMovieSession : ResponseMapper<MovieScheduleResponse, MovieSession> {

    override fun getMapper(): Function<MovieScheduleResponse, MovieSession> {
        return Function { scheduleResponse ->
            //Set MovieSession ID to 0 (zero) to insert it into ObjectBox DB with generated ID
            MovieSession(
                0,
                scheduleResponse.cinemaId,
                scheduleResponse.movieId,
                scheduleResponse.date,
                scheduleResponse.available
            )
        }
    }
}
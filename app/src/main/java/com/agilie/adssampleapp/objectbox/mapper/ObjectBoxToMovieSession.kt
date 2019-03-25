package com.agilie.adssampleapp.objectbox.mapper

import com.agilie.adssampleapp.domain.model.MovieSession
import com.agilie.adssampleapp.objectbox.data.MovieSessionObjectBox
import io.reactivex.functions.Function

class ObjectBoxToMovieSession : ObjectBoxMapper<MovieSessionObjectBox, MovieSession> {
    override fun getMapper(): Function<MovieSessionObjectBox, MovieSession> {
        return Function {
            MovieSession(it.id, it.cinemaId, it.movieId, it.date, it.available)
        }
    }
}
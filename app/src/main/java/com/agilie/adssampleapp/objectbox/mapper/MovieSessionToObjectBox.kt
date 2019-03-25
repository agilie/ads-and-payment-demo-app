package com.agilie.adssampleapp.objectbox.mapper

import com.agilie.adssampleapp.domain.model.MovieSession
import com.agilie.adssampleapp.objectbox.data.MovieSessionObjectBox
import io.reactivex.functions.Function

class MovieSessionToObjectBox : ObjectBoxMapper<MovieSession, MovieSessionObjectBox> {
    override fun getMapper(): Function<MovieSession, MovieSessionObjectBox> {
        return Function {
            MovieSessionObjectBox(it.id, it.cinemaId, it.movieId, it.date, it.available)
        }
    }
}
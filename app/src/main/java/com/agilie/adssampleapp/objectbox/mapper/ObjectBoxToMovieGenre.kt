package com.agilie.adssampleapp.objectbox.mapper

import com.agilie.adssampleapp.domain.model.MovieGenre
import com.agilie.adssampleapp.objectbox.data.MovieGenreObjectBox
import io.reactivex.functions.Function

class ObjectBoxToMovieGenre : ObjectBoxMapper<MovieGenreObjectBox, MovieGenre> {
    override fun getMapper(): Function<MovieGenreObjectBox, MovieGenre> {
        return Function { MovieGenre(it.id, it.name) }
    }
}
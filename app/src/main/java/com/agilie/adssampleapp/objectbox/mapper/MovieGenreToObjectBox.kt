package com.agilie.adssampleapp.objectbox.mapper

import com.agilie.adssampleapp.domain.model.MovieGenre
import com.agilie.adssampleapp.objectbox.data.MovieGenreObjectBox
import io.reactivex.functions.Function

class MovieGenreToObjectBox : ObjectBoxMapper<MovieGenre, MovieGenreObjectBox> {
    override fun getMapper(): Function<MovieGenre, MovieGenreObjectBox> {
        return Function { MovieGenreObjectBox(it.id, it.name) }
    }
}
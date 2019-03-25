package com.agilie.adssampleapp.objectbox.mapper

import com.agilie.adssampleapp.domain.model.Movie
import com.agilie.adssampleapp.domain.model.MovieGenre
import com.agilie.adssampleapp.objectbox.data.MovieObjectBox
import io.reactivex.functions.Function

class ObjectBoxToMovie : ObjectBoxMapper<MovieObjectBox, Movie> {
    override fun getMapper(): Function<MovieObjectBox, Movie> {
        return Function { movieOB ->
            Movie(
                    movieOB.id,
                    movieOB.title,
                    movieOB.overview,
                    movieOB.genres.map { MovieGenre(it.id, it.name) },
                    emptyList(),
                    movieOB.releaseDate,
                    movieOB.averageVote,
                    movieOB.backDrop,
                    movieOB.poster
            )
        }
    }
}
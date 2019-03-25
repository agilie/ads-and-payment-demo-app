package com.agilie.adssampleapp.objectbox.mapper

import com.agilie.adssampleapp.domain.model.Movie
import com.agilie.adssampleapp.objectbox.data.MovieGenreObjectBox
import com.agilie.adssampleapp.objectbox.data.MovieObjectBox
import io.objectbox.Box
import io.reactivex.functions.Function
import java.util.*

class MovieToObjectBox(private val box: Box<MovieObjectBox>) : ObjectBoxMapper<Movie, MovieObjectBox> {
    override fun getMapper(): Function<Movie, MovieObjectBox> {
        return Function { movie: Movie ->
            MovieObjectBox(
                movie.id,
                movie.title,
                movie.overview ?: "",
                movie.releaseDate ?: Date(),
                movie.averageVote ?: "",
                movie.backDrop ?: "",
                movie.poster ?: ""
            ).apply {
                box.attach(this)
                movie.genreIds
                    ?.map { MovieGenreObjectBox(it, "") }
                    ?.forEach { genres.add(it) }
            }
        }
    }
}
package com.agilie.adssampleapp.objectbox.mapper

import com.agilie.adssampleapp.domain.model.MovieDetails
import com.agilie.adssampleapp.objectbox.data.MovieDetailsObjectBox
import io.reactivex.functions.Function

class ObjectBoxToMovieDetails : Function<MovieDetailsObjectBox, MovieDetails> {

    override fun apply(objectBox: MovieDetailsObjectBox): MovieDetails {
        return MovieDetails(
                objectBox.id,
                objectBox.title,
                objectBox.tagline,
                objectBox.overview,
                objectBox.releaseDate,
                objectBox.homepage,
                objectBox.backdropPath,
                objectBox.posterPath
        )
    }
}
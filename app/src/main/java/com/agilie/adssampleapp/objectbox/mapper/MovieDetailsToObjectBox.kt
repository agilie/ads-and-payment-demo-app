package com.agilie.adssampleapp.objectbox.mapper

import com.agilie.adssampleapp.domain.model.MovieDetails
import com.agilie.adssampleapp.objectbox.data.MovieDetailsObjectBox
import io.reactivex.functions.Function

class MovieDetailsToObjectBox : Function<MovieDetails, MovieDetailsObjectBox> {

    override fun apply(movieDetails: MovieDetails): MovieDetailsObjectBox {
        return MovieDetailsObjectBox(
            movieDetails.id,
            movieDetails.title,
            movieDetails.tagline,
            movieDetails.overview,
            movieDetails.releaseDate,
            movieDetails.homepage,
            movieDetails.backdropPath,
            movieDetails.posterPath
        )
    }
}
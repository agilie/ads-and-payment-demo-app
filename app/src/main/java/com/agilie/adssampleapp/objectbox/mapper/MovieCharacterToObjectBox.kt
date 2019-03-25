package com.agilie.adssampleapp.objectbox.mapper

import com.agilie.adssampleapp.domain.model.MovieCharacter
import com.agilie.adssampleapp.objectbox.data.MovieCharacterObjectBox
import io.reactivex.functions.Function

class MovieCharacterToObjectBox : Function<MovieCharacter, MovieCharacterObjectBox> {

    override fun apply(movieCharacter: MovieCharacter): MovieCharacterObjectBox {
        return MovieCharacterObjectBox(
            movieCharacter.id,
            movieCharacter.movieId,
            movieCharacter.character,
            movieCharacter.name,
            movieCharacter.order,
            movieCharacter.profileImage
        )
    }
}
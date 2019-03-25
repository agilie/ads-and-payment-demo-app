package com.agilie.adssampleapp.objectbox.mapper

import com.agilie.adssampleapp.domain.model.MovieCharacter
import com.agilie.adssampleapp.objectbox.data.MovieCharacterObjectBox
import io.reactivex.functions.Function

class ObjectBoxToMovieCharacter : Function<MovieCharacterObjectBox, MovieCharacter> {

    override fun apply(objectBox: MovieCharacterObjectBox): MovieCharacter {
        return MovieCharacter(
                objectBox.id,
                objectBox.movieId,
                objectBox.character,
                objectBox.name,
                objectBox.order,
                objectBox.profileImage
        )
    }
}
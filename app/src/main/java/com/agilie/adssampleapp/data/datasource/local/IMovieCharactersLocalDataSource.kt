package com.agilie.adssampleapp.data.datasource.local

import com.agilie.adssampleapp.domain.model.MovieCharacter
import io.reactivex.Completable
import io.reactivex.Single

interface IMovieCharactersLocalDataSource {
    fun getMovieCharacters(movieId: Long): Single<List<MovieCharacter>>

    fun saveMovieCharacters(characters: List<MovieCharacter>): Completable
}
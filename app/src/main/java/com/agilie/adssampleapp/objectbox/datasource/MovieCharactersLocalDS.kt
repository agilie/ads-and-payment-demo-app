package com.agilie.adssampleapp.objectbox.datasource

import com.agilie.adssampleapp.data.datasource.local.IMovieCharactersLocalDataSource
import com.agilie.adssampleapp.domain.model.MovieCharacter
import com.agilie.adssampleapp.objectbox.data.MovieCharacterObjectBox
import com.agilie.adssampleapp.objectbox.data.MovieCharacterObjectBox_
import com.agilie.adssampleapp.objectbox.mapper.MovieCharacterToObjectBox
import com.agilie.adssampleapp.objectbox.mapper.ObjectBoxToMovieCharacter
import io.objectbox.BoxStore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class MovieCharactersLocalDS(boxStore: BoxStore) : IMovieCharactersLocalDataSource {

    private val box = boxStore.boxFor(MovieCharacterObjectBox::class.java)

    override fun getMovieCharacters(movieId: Long): Single<List<MovieCharacter>> =
        Observable
            .fromIterable(
                box.query()
                    .equal(MovieCharacterObjectBox_.movieId, movieId)
                    .build()
                    .find()
            )
            .map(ObjectBoxToMovieCharacter())
            .toList()

    override fun saveMovieCharacters(characters: List<MovieCharacter>): Completable =
        Observable.fromIterable(characters)
            .map(MovieCharacterToObjectBox())
            .toList()
            .doOnSuccess { box.put(it) }
            .ignoreElement()
}
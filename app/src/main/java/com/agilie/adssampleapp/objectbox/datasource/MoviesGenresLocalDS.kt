package com.agilie.adssampleapp.objectbox.datasource

import com.agilie.adssampleapp.data.datasource.local.IMoviesGenresLocalDataSource
import com.agilie.adssampleapp.domain.model.MovieGenre
import com.agilie.adssampleapp.objectbox.data.MovieGenreObjectBox
import com.agilie.adssampleapp.objectbox.mapper.MovieGenreToObjectBox
import com.agilie.adssampleapp.objectbox.mapper.ObjectBoxToMovieGenre
import io.objectbox.BoxStore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class MoviesGenresLocalDS(private val boxStore: BoxStore) : IMoviesGenresLocalDataSource {

    private val box = boxStore.boxFor(MovieGenreObjectBox::class.java)

    override fun getMoviesGenres(): Single<List<MovieGenre>> =
        Observable.fromIterable(box.all)
            .map(ObjectBoxToMovieGenre().getMapper())
            .toList()

    override fun saveMoviesGenres(genres: List<MovieGenre>): Completable =
        Observable.fromIterable(genres)
            .map(MovieGenreToObjectBox().getMapper())
            .toList()
            .doOnSuccess { box.put(it) }
            .ignoreElement()
}
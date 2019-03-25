package com.agilie.adssampleapp.objectbox.datasource

import com.agilie.adssampleapp.data.datasource.local.IMoviesLocalDataSource
import com.agilie.adssampleapp.domain.model.Movie
import com.agilie.adssampleapp.objectbox.data.MovieObjectBox
import com.agilie.adssampleapp.objectbox.mapper.MovieToObjectBox
import com.agilie.adssampleapp.objectbox.mapper.ObjectBoxToMovie
import io.objectbox.BoxStore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class MoviesLocalDS(boxStore: BoxStore) : IMoviesLocalDataSource {

    private val box = boxStore.boxFor(MovieObjectBox::class.java)
    private val toMovieMapper by lazy { ObjectBoxToMovie().getMapper() }
    private val toObjectBoxMapper by lazy { MovieToObjectBox(box).getMapper() }

    override fun getMovies(): Single<List<Movie>> =
        Observable.fromIterable(box.all)
            .map(toMovieMapper)
            .toList()

    override fun saveMovies(movies: List<Movie>): Completable =
        Observable.fromIterable(movies)
            .map(toObjectBoxMapper)
            .toList()
            .doOnSuccess { box.put(it) }
            .ignoreElement()
}
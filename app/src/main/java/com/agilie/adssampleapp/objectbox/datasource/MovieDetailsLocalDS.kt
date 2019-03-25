package com.agilie.adssampleapp.objectbox.datasource

import com.agilie.adssampleapp.data.datasource.local.IMovieDetailsLocalDataSource
import com.agilie.adssampleapp.domain.model.MovieDetails
import com.agilie.adssampleapp.objectbox.data.MovieDetailsObjectBox
import com.agilie.adssampleapp.objectbox.mapper.MovieDetailsToObjectBox
import com.agilie.adssampleapp.objectbox.mapper.ObjectBoxToMovieDetails
import io.objectbox.BoxStore
import io.reactivex.Completable
import io.reactivex.Single

class MovieDetailsLocalDS(boxStore: BoxStore) : IMovieDetailsLocalDataSource {

    private val box = boxStore.boxFor(MovieDetailsObjectBox::class.java)

    override fun getMovieDetails(movieId: Long): Single<MovieDetails> =
        Single.just(movieId).map { box.get(it) }.map(ObjectBoxToMovieDetails())


    override fun saveMovieDetails(details: MovieDetails): Completable =
        Completable.fromAction {
            val objectBox = MovieDetailsToObjectBox().apply(details)
            box.put(objectBox)
        }
}
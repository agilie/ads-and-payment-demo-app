package com.agilie.adssampleapp.objectbox.datasource

import com.agilie.adssampleapp.data.datasource.local.IMoviesScheduleLocalDataSource
import com.agilie.adssampleapp.domain.model.MovieSession
import com.agilie.adssampleapp.objectbox.data.MovieSessionObjectBox
import com.agilie.adssampleapp.objectbox.data.MovieSessionObjectBox_
import com.agilie.adssampleapp.objectbox.mapper.MovieSessionToObjectBox
import com.agilie.adssampleapp.objectbox.mapper.ObjectBoxToMovieSession
import io.objectbox.BoxStore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class MoviesScheduleLocalDS(boxStore: BoxStore) : IMoviesScheduleLocalDataSource {

    private val box = boxStore.boxFor(MovieSessionObjectBox::class.java)
    private val toMovieSessionMapper by lazy { ObjectBoxToMovieSession().getMapper() }
    private val toObjectBoxMapper by lazy { MovieSessionToObjectBox().getMapper() }

    override fun getMovieSchedule(cinemaId: Int, movieId: Long): Single<List<MovieSession>> =
        Observable.fromCallable { selectSchedules(cinemaId, movieId) }
            .flatMapIterable { it }
            .map(toMovieSessionMapper)
            .toList()

    override fun saveMovieSchedule(sessions: List<MovieSession>): Completable =
        Observable.fromIterable(sessions)
            .map(toObjectBoxMapper)
            .toList()
            .doOnSuccess { box.put(it) }
            .ignoreElement()

    private fun selectSchedules(cinemaId: Int, movieId: Long): List<MovieSessionObjectBox> {
        return box.query().run {
            equal(MovieSessionObjectBox_.cinemaId, cinemaId.toLong())
            equal(MovieSessionObjectBox_.movieId, movieId)
            return@run build().find()
        }
    }
}
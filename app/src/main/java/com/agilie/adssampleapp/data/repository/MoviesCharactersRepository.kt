package com.agilie.adssampleapp.data.repository

import com.agilie.adssampleapp.data.datasource.local.IMovieCharactersLocalDataSource
import com.agilie.adssampleapp.data.datasource.remote.IMovieCharactersRemoteDataSource
import com.agilie.adssampleapp.domain.model.MovieCharacter
import io.reactivex.Single
import io.reactivex.functions.Function

class MoviesCharactersRepository(
    private val localDS: IMovieCharactersLocalDataSource,
    private val remoteDS: IMovieCharactersRemoteDataSource
) {

    fun getMoviesCast(movieId: Long): Single<List<MovieCharacter>> {
        return localDS.getMovieCharacters(movieId)
            .flatMap(Function {
                if (it.isEmpty()) {
                    return@Function remoteDS.getMovieDetails(movieId)
                        .flatMap { loaded ->
                            localDS.saveMovieCharacters(loaded)
                                .andThen(Single.just(loaded))
                        }
                } else {
                    return@Function Single.just(it)
                }
            })
    }
}
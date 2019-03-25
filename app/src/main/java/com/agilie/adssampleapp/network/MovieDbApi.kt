package com.agilie.adssampleapp.network

import com.agilie.adssampleapp.data.model.MoviesGenresResponse
import com.agilie.adssampleapp.data.model.MoviesListResponse
import com.agilie.adssampleapp.domain.model.MovieCredits
import com.agilie.adssampleapp.domain.model.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbApi {

    @GET(URL_UPCOMING_MOVIES)
    fun getUpcomingMovies(
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_LANGUAGE) language: String? = null,
        @Query(QUERY_PAGE) page: Int = 1
    ): Single<MoviesListResponse>

    @GET(URL_MOVIE_DETAILS)
    fun getMovieDetails(
        @Path(PATH_PART_MOVIE_ID) movieId: Long,
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_LANGUAGE) language: String? = null
    ): Single<MovieDetails>

    @GET(URL_MOVIE_CREDITS)
    fun getMovieCredits(
        @Path(PATH_PART_MOVIE_ID) movieId: Long,
        @Query(QUERY_API_KEY) apiKey: String
    ): Single<MovieCredits>

    @GET(URL_MOVIES_GENRES)
    fun getMoviesGenres(@Query(QUERY_API_KEY) apiKey: String): Single<MoviesGenresResponse>
}
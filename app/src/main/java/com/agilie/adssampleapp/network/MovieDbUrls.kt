package com.agilie.adssampleapp.network

const val PATH_PART_MOVIE_ID = "movie_id"

const val QUERY_API_KEY = "api_key"
const val QUERY_LANGUAGE = "language"
const val QUERY_PAGE = "page"

const val URL_MOVIE_DETAILS = "movie/{$PATH_PART_MOVIE_ID}"
const val URL_MOVIE_CREDITS = "movie/{$PATH_PART_MOVIE_ID}/credits"
const val URL_UPCOMING_MOVIES = "movie/upcoming"
const val URL_MOVIES_GENRES = "genre/movie/list"

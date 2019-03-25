package com.agilie.adssampleapp.data.model

import com.agilie.adssampleapp.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class MoviesListResponse(
        @SerializedName("results") val movies: List<Movie>,
        @SerializedName("page") val currentPage: Int,
        @SerializedName("total_results") val totalResults: Int,
        @SerializedName("total_pages") val totalPages: Int
)
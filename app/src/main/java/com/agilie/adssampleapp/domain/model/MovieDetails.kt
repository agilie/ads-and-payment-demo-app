package com.agilie.adssampleapp.domain.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class MovieDetails(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("tagline") val tagline: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("release_date") val releaseDate: Date,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("poster_path") val posterPath: String?
)
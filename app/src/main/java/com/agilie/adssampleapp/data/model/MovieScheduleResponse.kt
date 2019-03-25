package com.agilie.adssampleapp.data.model

import com.agilie.adssampleapp.domain.model.MovieSessionAvailable
import com.google.gson.annotations.SerializedName
import java.util.*

data class MovieScheduleResponse(
    @SerializedName("cinema_id") val cinemaId: Int,
    @SerializedName("movie_id") val movieId: Long,
    @SerializedName("date") val date: Date,
    @SerializedName("available") val available: MovieSessionAvailable
)
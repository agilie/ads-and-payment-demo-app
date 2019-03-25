package com.agilie.adssampleapp.data.model

import com.agilie.adssampleapp.domain.model.MovieGenre
import com.google.gson.annotations.SerializedName

data class MoviesGenresResponse(@SerializedName("genres") val genres: List<MovieGenre>)
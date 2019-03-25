package com.agilie.adssampleapp.domain.model

import com.agilie.adssampleapp.network.DateConverter
import com.google.gson.annotations.SerializedName
import java.util.*

data class Movie(
        @SerializedName("id") val id: Long,
        @SerializedName("title") val title: String,
        @SerializedName("overview") val overview: String?,
        @SerializedName("genres") val genres: List<MovieGenre>? = emptyList(),
        @SerializedName("genre_ids") val genreIds: List<Long>? = emptyList(),
        @SerializedName("release_date") val releaseDate: Date?,
        @SerializedName("vote_average") val averageVote: String?,
        @SerializedName("backdrop_path") val backDrop: String?,
        @SerializedName("poster_path") val poster: String?
) {
    fun getGenresString(): String = genres?.joinToString { it.name } ?: ""

    fun getRelease(): String =
        if (releaseDate == null) {
            ""
        } else {
            DateConverter().responseDateFormatter.format(releaseDate)
        }

    fun getReleaseYear(): String =
        if (releaseDate == null) {
            ""
        } else {
            Calendar.getInstance().run {
                time = releaseDate
                return@run get(Calendar.YEAR).toString()
            }
        }
}
package com.agilie.adssampleapp.objectbox.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import java.util.*

@Entity
class MovieObjectBox(
    @Id(assignable = true) var id: Long = 0,
    val title: String = "",
    val overview: String = "",
    val releaseDate: Date,
    val averageVote: String = "",
    val backDrop: String = "",
    val poster: String = ""
) {
    lateinit var genres: ToMany<MovieGenreObjectBox>
}
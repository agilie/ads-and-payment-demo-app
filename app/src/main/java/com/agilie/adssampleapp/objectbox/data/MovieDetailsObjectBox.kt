package com.agilie.adssampleapp.objectbox.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

@Entity
data class MovieDetailsObjectBox(
    @Id(assignable = true) var id: Long = 0,
    var title: String,
    var tagline: String?,
    var overview: String?,
    var releaseDate: Date,
    var homepage: String?,
    var backdropPath: String?,
    var posterPath: String?
)
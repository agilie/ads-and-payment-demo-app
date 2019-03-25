package com.agilie.adssampleapp.domain.model

import java.io.Serializable
import java.util.*

data class MovieSession(
    val id: Long,
    val cinemaId: Int,
    val movieId: Long,
    val date: Date,
    val available: MovieSessionAvailable
) : Serializable
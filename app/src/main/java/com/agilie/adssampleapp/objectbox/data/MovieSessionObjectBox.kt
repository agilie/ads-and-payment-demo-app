package com.agilie.adssampleapp.objectbox.data

import com.agilie.adssampleapp.domain.model.MovieSessionAvailable
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

@Entity
class MovieSessionObjectBox(
    @Id var id: Long = 0,
    val cinemaId: Int,
    val movieId: Long,
    val date: Date,
    @Convert(converter = EnumConverter::class, dbType = Int::class)
    val available: MovieSessionAvailable
)
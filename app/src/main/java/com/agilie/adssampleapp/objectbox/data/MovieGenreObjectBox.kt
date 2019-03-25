package com.agilie.adssampleapp.objectbox.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class MovieGenreObjectBox(
    @Id(assignable = true) var id: Long = 0,
    val name: String
)
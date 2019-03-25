package com.agilie.adssampleapp.objectbox.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class MovieCharacterObjectBox(
    @Id(assignable = true) var id: Long = 0,
    val movieId: Long,
    val character: String,
    val name: String,
    val order: Int,
    val profileImage: String?
)
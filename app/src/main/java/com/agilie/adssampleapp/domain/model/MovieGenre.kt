package com.agilie.adssampleapp.domain.model

import com.google.gson.annotations.SerializedName

data class MovieGenre(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
)
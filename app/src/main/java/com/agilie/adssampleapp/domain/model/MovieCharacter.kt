package com.agilie.adssampleapp.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieCharacter(
    @Expose(serialize = false, deserialize = false) var movieId: Long,
    @SerializedName("id") val id: Long,
    @SerializedName("character") val character: String,
    @SerializedName("name") val name: String,
    @SerializedName("order") val order: Int,
    @SerializedName("profile_path") val profileImage: String?
)
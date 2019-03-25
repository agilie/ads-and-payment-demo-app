package com.agilie.adssampleapp.domain.model

import com.google.android.gms.maps.model.LatLng

data class City(
    val id: Int,
    val name: String,
    val center: LatLng
)
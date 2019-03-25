package com.agilie.adssampleapp.domain.model

import com.google.android.gms.maps.model.LatLng

data class Cinema(
        val id: Int,
        val name: String,
        val city: City,
        val coordinates: LatLng,
        val logoUrl: String
)
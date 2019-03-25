package com.agilie.adssampleapp.data.repository

import com.agilie.adssampleapp.domain.model.Cinema
import com.agilie.adssampleapp.domain.model.City
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class CinemaRepository {
    val cinemaList: List<Cinema> by lazy { initCinemaList() }

    fun fetchCinemaList(): Single<List<Cinema>>{
        return Single.just(cinemaList).subscribeOn(Schedulers.io())
    }

    private fun initCinemaList(): List<Cinema> {
        val city = City(1, "Dnipro", LatLng(48.46666667, 35.01805556))
        return listOf(
                Cinema(1, "Dafi", city, LatLng(48.4253882, 35.0218291), "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSWzozSu9xQA06DF9GLb-JzPQ5Z8i3db4wmG9IOiRf1TojOl7U3"),
                Cinema(2, "Most-Kino", city, LatLng(48.466846, 35.0510719), "https://gorod.dp.ua/pic/placeimages/07/3096/logo.jpg"),
                Cinema(3, "Multiplex", city, LatLng(48.5285775, 35.0327343), "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1glN6vsZA3wGDSbTo4jNdZsyB9VzqGT19of2TyZaeUBbhy9RO")
        )
    }
}
package com.agilie.adssampleapp.objectbox.mapper

import io.reactivex.functions.Function

interface ObjectBoxMapper<T, R> {
    fun getMapper(): Function<T, R>
}
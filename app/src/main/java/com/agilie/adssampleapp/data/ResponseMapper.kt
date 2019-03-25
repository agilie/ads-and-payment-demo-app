package com.agilie.adssampleapp.data

import io.reactivex.functions.Function

interface ResponseMapper<INPUT, RESULT> {
    fun getMapper(): Function<INPUT, RESULT>
}
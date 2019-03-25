package com.agilie.adssampleapp.objectbox.data

import com.agilie.adssampleapp.domain.model.MovieSessionAvailable
import io.objectbox.converter.PropertyConverter

class EnumConverter : PropertyConverter<MovieSessionAvailable, Int> {
    override fun convertToDatabaseValue(entityProperty: MovieSessionAvailable): Int {
        return entityProperty.ordinal
    }

    override fun convertToEntityProperty(databaseValue: Int?): MovieSessionAvailable {
        return if (databaseValue == null) {
            MovieSessionAvailable.SOLD_OUT
        } else {
            MovieSessionAvailable.values()[databaseValue]
        }
    }
}